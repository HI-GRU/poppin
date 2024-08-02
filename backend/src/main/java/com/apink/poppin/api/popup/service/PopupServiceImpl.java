package com.apink.poppin.api.popup.service;

import com.apink.poppin.api.manager.entity.Manager;
import com.apink.poppin.api.manager.repository.ManagerRepository;
import com.apink.poppin.api.popup.dto.PopupDTO;
import com.apink.poppin.api.popup.dto.PopupRequestDTO;
import com.apink.poppin.api.reservation.entity.PreReservationInfo;
import com.apink.poppin.api.reservation.repository.PreReservationInfoRepository;
import com.apink.poppin.api.reservation.dto.PreReservationRequestDTO;
import com.apink.poppin.api.reservation.dto.PreReservationResponseDTO;
import com.apink.poppin.api.popup.entity.Popup;
import com.apink.poppin.api.reservation.dto.PreStatementRequestDTO;
import com.apink.poppin.api.reservation.dto.PreStatementResponseDTO;
import com.apink.poppin.api.reservation.entity.PreReservation;
import com.apink.poppin.api.popup.repository.PopupRepository;
import com.apink.poppin.api.reservation.entity.ReservationStatement;
import com.apink.poppin.api.reservation.repository.PreReservationRepository;
import com.apink.poppin.api.reservation.repository.ReservationStatementRepository;
import com.apink.poppin.api.user.entity.User;
import com.apink.poppin.api.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PopupServiceImpl implements PopupService {

    private final PopupRepository popupRepository;
    private final PreReservationRepository preReservationRepository;
    private final UserRepository userRepository;
    private final ReservationStatementRepository reservationStatementRepository;
    private final ManagerRepository managerRepository;
    private final PreReservationInfoRepository preReservationInfoRepository;


    // 팝업 전체 목록 조회 및 검색
    public List<PopupDTO> getPopupList(String keyword) {
        List<Popup> popups = popupRepository.findAllByNameContaining(keyword);
        return popups.stream()
                .map(popup -> new PopupDTO(popup.getPopupId(), popup.getName(), popup.getStartDate(), popup.getEndDate(), popup.getHeart()))
                .collect(Collectors.toList());
    }

    // 팝업 상세 조회
    public PopupDTO getPopup(Long popupId) {
        Popup popup = popupRepository.findById(popupId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid popup ID"));
        return new PopupDTO(popup.getPopupId(), popup.getName(), popup.getStartDate(), popup.getEndDate(), popup.getHeart());
    }

    // 인기 팝업 조회
    public List<PopupDTO> getPopupRank() {
        List<Popup> list = popupRepository.findAllByOrderByHeartDesc();

        return list.stream()
                .map(popup -> new PopupDTO(popup.getPopupId(), popup.getName(), popup.getStartDate(), popup.getEndDate(), popup.getHeart()))
                .collect(Collectors.toList());
    }

    // 유사 팝업 조회
//    public List<PopupDTO> getSimilarPopup(long popupId) {
//
//    }

    // 오픈 예정 팝업 조회
    public List<PopupDTO> getOpenPopup() {
        LocalDateTime now = LocalDateTime.now();
        List<Popup> popups = popupRepository.findAllByStartDateAfter(now);
        return popups.stream()
                .map(popup -> new PopupDTO(popup.getPopupId(), popup.getName(), popup.getStartDate(), popup.getEndDate(), popup.getHeart()))
                .collect(Collectors.toList());
    }

    // 사전 예약
    @Override
    @Transactional
    public PreReservation createPreReservation(PreReservationRequestDTO req) {
        // 유저 확인
        User user = userRepository.findUserByUserTsid(req.getUserTsid())
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Tsid"));
        // 팝업 확인
        Popup popup = popupRepository.findById(req.getPopupId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid popup ID"));
        // 예약 상태 확인 ?
        ReservationStatement reservationStatement = reservationStatementRepository.findById(req.getReservationStatementId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid reservation statement ID"));

        PreReservation preReservation = PreReservation.builder()
                .user(user)
                .popup(popup)
                .reservationDate(req.getReservationDate())
                .reservationTime(req.getReservationTime())
                .reservationCount(req.getReservationCount())
                .reservationStatement(reservationStatement)
                .build();

        return preReservationRepository.save(preReservation);
    }

    // 날짜 별 사전예약자 정보 (매니저)
    // 유저 코드 합치면 유저 정보 같이 보여주기!
    public List<PreReservationResponseDTO> getPreReservationsByDate(Date reservationDate) {
        List<PreReservation> preReservations = preReservationRepository.findAllByReservationDate(reservationDate);

        return preReservations.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    // 사전 예약 상태 정보 변경하기
    @Transactional
    @Override
    public PreStatementResponseDTO changePreReservation(PreStatementRequestDTO reqDto) {

        // 유저 확인
        User user = userRepository.findUserByUserTsid(reqDto.getUserTsid())
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Tsid"));
        // 팝업 확인
        Popup popup = popupRepository.findById(reqDto.getPopupId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid popup ID"));

        PreReservation preReservation = preReservationRepository.findById(reqDto.getPreReservationId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid pre-reservation ID"));

        ReservationStatement newStatement = reservationStatementRepository.findById(reqDto.getReservationStatementId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid reservationStatement ID"));

        preReservation.setReservationStatement(newStatement);
        preReservationRepository.save(preReservation);

        return PreStatementResponseDTO.builder()
                .preReservationId(preReservation.getPreReservationId())
                .reservationStatementId(newStatement.getReservationStatementId())
                .userTsid(preReservation.getUser().getUserTsid())
                .popupId(preReservation.getPopup().getPopupId())
                .build();
    }

    // 팝업 등록 (사전 예약 없이)
    @Transactional
    @Override
    public Popup createPopupOnly(PopupRequestDTO reqDto) {
        // 매니저 확인
        Manager manager = managerRepository.findByManagerTsid(reqDto.getManagerTsid())
                .orElseThrow(() -> new IllegalArgumentException("Invalid manager Tsid"));

        Popup popup = Popup.builder()
                .manager(manager)
                .name(reqDto.getName())
                .startDate(reqDto.getStartDate())
                .endDate(reqDto.getEndDate())
                .hours(reqDto.getHours())
                .description(reqDto.getDescription())
                .snsUrl(reqDto.getSnsUrl())
                .pageUrl(reqDto.getPageUrl())
                .content(reqDto.getContent())
                .lat(reqDto.getLat())
                .lon(reqDto.getLon())
                .build();

        popupRepository.save(popup);

        return popup;
    }


    // 팝업 등록 (사전예약까지)
    @Transactional
    @Override
    public void createPopupWithPreReservation(PopupRequestDTO reqDto) {
        // 매니저 확인
        Manager manager = managerRepository.findByManagerTsid(reqDto.getManagerTsid())
                .orElseThrow(() -> new IllegalArgumentException("Invalid manager Tsid"));

        Popup popup = Popup.builder()
                .manager(manager)
                .name(reqDto.getName())
                .startDate(reqDto.getStartDate())
                .endDate(reqDto.getEndDate())
                .hours(reqDto.getHours())
                .description(reqDto.getDescription())
                .snsUrl(reqDto.getSnsUrl())
                .pageUrl(reqDto.getPageUrl())
                .content(reqDto.getContent())
                .lat(reqDto.getLat())
                .lon(reqDto.getLon())
                .build();

        popupRepository.save(popup);

        // PreReservationInfo 엔티티 생성
        PreReservationInfo preReservationInfo = PreReservationInfo.builder()
                .popup(popup)
                .preReservationOpenAt(reqDto.getPreReservationOpenAt())
                .term(reqDto.getTerm())
                .maxPeoplePerSession(reqDto.getMaxPeoplePerSession())
                .maxReservationsPerPerson(reqDto.getMaxReservationsPerPersson())
                .warning(reqDto.getWarning())
                .build();

        preReservationInfoRepository.save(preReservationInfo);

    }

    // 팝업 수정
    @Transactional
    @Override
    public Popup updatePopup(PopupRequestDTO reqDto, long popupId) {
        // 매니저 확인
        Manager manager = managerRepository.findByManagerTsid(reqDto.getManagerTsid())
                .orElseThrow(() -> new IllegalArgumentException("Invalid manager Tsid"));

        // 팝업 확인
        Popup popup = popupRepository.findById(popupId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid popup ID"));

        popup.updatePopup(reqDto);

//        popupRepository.save(popup);

        return popup;
    }


    // DTO 변환
    private PreReservationResponseDTO convertToResponseDTO(PreReservation preReservation) {
        return PreReservationResponseDTO.builder()
                .preReservationId(preReservation.getPreReservationId())
                .userTsid(preReservation.getUser().getUserTsid())
                .popupId(preReservation.getPopup().getPopupId())
                .reservationDate(preReservation.getReservationDate())
                .reservationTime(preReservation.getReservationTime())
                .reservationCount(preReservation.getReservationCount())
                .createdAt(preReservation.getCreatedAt())
                .reservationStatementId(preReservation.getReservationStatement().getReservationStatementId())
                .build();
    }

}
