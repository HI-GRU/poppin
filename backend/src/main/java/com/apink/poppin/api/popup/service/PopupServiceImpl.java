package com.apink.poppin.api.popup.service;

import com.apink.poppin.api.heart.entity.Heart;
import com.apink.poppin.api.heart.repository.HeartRepository;
import com.apink.poppin.api.manager.entity.Manager;
import com.apink.poppin.api.manager.repository.ManagerRepository;
import com.apink.poppin.api.popup.dto.PopupDTO;
import com.apink.poppin.api.popup.dto.PopupRequestDTO;
import com.apink.poppin.api.popup.dto.PopupWithPreReservationDTO;
import com.apink.poppin.api.popup.dto.SimilarPopupRequestDto;
import com.apink.poppin.api.popup.entity.Category;
import com.apink.poppin.api.popup.entity.Popup;
import com.apink.poppin.api.popup.entity.PopupCategory;
import com.apink.poppin.api.popup.entity.PopupImage;
import com.apink.poppin.api.popup.repository.CategoryRepository;
import com.apink.poppin.api.popup.repository.PopupCategoryRepository;
import com.apink.poppin.api.popup.repository.PopupImageRepository;
import com.apink.poppin.api.popup.repository.PopupRepository;
import com.apink.poppin.api.reservation.dto.PreReservationRequestDTO;
import com.apink.poppin.api.reservation.dto.PreReservationResponseDTO;
import com.apink.poppin.api.reservation.dto.PreStatementRequestDTO;
import com.apink.poppin.api.reservation.dto.PreStatementResponseDTO;
import com.apink.poppin.api.reservation.entity.OnsiteReservation;
import com.apink.poppin.api.reservation.entity.PreReservation;
import com.apink.poppin.api.reservation.entity.PreReservationInfo;
import com.apink.poppin.api.reservation.entity.ReservationStatement;
import com.apink.poppin.api.reservation.repository.OnsiteReservationRepository;
import com.apink.poppin.api.reservation.repository.PreReservationInfoRepository;
import com.apink.poppin.api.reservation.repository.PreReservationRepository;
import com.apink.poppin.api.reservation.repository.ReservationStatementRepository;
import com.apink.poppin.api.user.entity.User;
import com.apink.poppin.api.user.repository.UserRepository;
import com.apink.poppin.common.exception.dto.BusinessLogicException;
import com.apink.poppin.common.exception.dto.ExceptionCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class PopupServiceImpl implements PopupService {

    private final PopupRepository popupRepository;
    private final PreReservationRepository preReservationRepository;
    private final UserRepository userRepository;
    private final ReservationStatementRepository reservationStatementRepository;
    private final ManagerRepository managerRepository;
    private final PreReservationInfoRepository preReservationInfoRepository;
    private final PopupImageRepository popupImageRepository;
    private final FileStorageService fileStorageService;
    private final PopupCategoryRepository popupCategoryRepository;
    private final CategoryRepository categoryRepository;
    private final HeartRepository heartRepository;
    private final OnsiteReservationRepository onsiteReservationRepository;


    @Value("${file.upload-dir}")
    private String uploadDir;

    // 팝업 전체 목록 조회 및 검색
    public List<PopupDTO> getPopupList(String keyword) {
        List<Popup> popups = popupRepository.findAllByNameContaining(keyword);

        return popups.stream()
                .filter(popup -> !popup.isDeleted())
                .map(popup -> {
                    List<String> images = popupImageRepository.findAllByPopup_PopupId(popup.getPopupId())
                            .stream()
                            .sorted((img1, img2) -> Integer.compare(img1.getSeq(), img2.getSeq()))
                            .map(PopupImage::getImg)
                            .toList();

                    List<String> categories = popupCategoryRepository.findByPopup(popup)
                            .stream()
                            .map(popupCategory -> popupCategory.getCategory().getName())
                            .toList();

                    return PopupDTO.builder()
                            .popupId(popup.getPopupId())
                            .name(popup.getName())
                            .startDate(popup.getStartDate())
                            .endDate(popup.getEndDate())
                            .hours(popup.getHours())
                            .snsUrl(popup.getSnsUrl())
                            .pageUrl(popup.getPageUrl())
                            .content(popup.getContent())
                            .description(popup.getDescription())
                            .address(popup.getAddress())
                            .lat(popup.getLat())
                            .lon(popup.getLon())
                            .heart(popup.getHeart())
                            .hit(popup.getHit())
                            .rating(popup.getRating())
                            .managerTsId(popup.getManager().getManagerTsid())
                            .images(images)
                            .categories(categories)
                            .checkPreReservation(
                                    preReservationInfoRepository.existsByPopup(popup)
                            )
                            .build();
                })
                .collect(Collectors.toList());
    }

    // 팝업 상세 조회
    public PopupDTO getPopup(Long popupId) {
        Popup popup = popupRepository.findById(popupId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid popup ID"));

        if(popup.isDeleted())
            throw new BusinessLogicException(ExceptionCode.POPUP_NOT_FOUND);

        List<String> images = popupImageRepository.findAllByPopup_PopupId(popup.getPopupId())
                .stream()
                .sorted((img1, img2) -> Integer.compare(img1.getSeq(), img2.getSeq()))
                .map(PopupImage::getImg)
                .toList();

        List<String> categories = popupCategoryRepository.findByPopup(popup)
                .stream()
                .map(popupCategory -> popupCategory.getCategory().getName())
                .toList();


        return PopupDTO.builder()
                .popupId(popup.getPopupId())
                .name(popup.getName())
                .startDate(popup.getStartDate())
                .endDate(popup.getEndDate())
                .hours(popup.getHours())
                .snsUrl(popup.getSnsUrl())
                .pageUrl(popup.getPageUrl())
                .content(popup.getContent())
                .description(popup.getDescription())
                .address(popup.getAddress())
                .lat(popup.getLat())
                .lon(popup.getLon())
                .heart(popup.getHeart())
                .hit(popup.getHit())
                .rating(popup.getRating())
                .managerTsId(popup.getManager().getManagerTsid())
                .images(images)
                .categories(categories)
                .checkPreReservation(preReservationInfoRepository.existsByPopup(popup))
                .build();
    }

    // 팝업 상세 조회 (+ 사전예약 정보)
    @Override
    public PopupWithPreReservationDTO getPopupWithPreReservation(Long popupId) {
        Popup popup = popupRepository.findById(popupId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid popup ID"));

        if(popup.isDeleted())
            throw new BusinessLogicException(ExceptionCode.POPUP_NOT_FOUND);

        List<String> images = popupImageRepository.findAllByPopup_PopupId(popup.getPopupId())
                .stream()
                .sorted((img1, img2) -> Integer.compare(img1.getSeq(), img2.getSeq()))
                .map(PopupImage::getImg)
                .toList();

        List<String> categories = popupCategoryRepository.findByPopup(popup)
                .stream()
                .map(popupCategory -> popupCategory.getCategory().getName())
                .toList();

        PreReservationInfo preInfo = preReservationInfoRepository.findByPopup(popup);

        return PopupWithPreReservationDTO.builder()
                .popupId(popup.getPopupId())
                .name(popup.getName())
                .startDate(popup.getStartDate())
                .endDate(popup.getEndDate())
                .hours(popup.getHours())
                .snsUrl(popup.getSnsUrl())
                .pageUrl(popup.getPageUrl())
                .content(popup.getContent())
                .description(popup.getDescription())
                .address(popup.getAddress())
                .lat(popup.getLat())
                .lon(popup.getLon())
                .heart(popup.getHeart())
                .hit(popup.getHit())
                .rating(popup.getRating())
                .managerTsId(popup.getManager().getManagerTsid())
                .images(images)
                .categories(categories)
                .checkPreReservation(preReservationInfoRepository.existsByPopup(popup))
                .preReservationOpenAt(preInfo.getPreReservationOpenAt())
                .term(preInfo.getTerm())
                .maxPeoplePerSession(preInfo.getMaxPeoplePerSession())
                .maxReservationsPerPerson(preInfo.getMaxReservationsPerPerson())
                .warning(preInfo.getWarning())
                .build();
    }


    // 인기 팝업 조회
    public List<PopupDTO> getPopupRank() {
        List<Popup> list = popupRepository.findAllByOrderByHeartDesc();

        return list.stream()
                .filter(popup -> !popup.isDeleted())
                .map(popup -> PopupDTO.builder()
                        .popupId(popup.getPopupId())
                        .name(popup.getName())
                        .startDate(popup.getStartDate())
                        .endDate(popup.getEndDate())
                        .hours(popup.getHours())
                        .snsUrl(popup.getSnsUrl())
                        .pageUrl(popup.getPageUrl())
                        .content(popup.getContent())
                        .description(popup.getDescription())
                        .address(popup.getAddress())
                        .lat(popup.getLat())
                        .lon(popup.getLon())
                        .heart(popup.getHeart())
                        .hit(popup.getHit())
                        .rating(popup.getRating())
                        .managerTsId(popup.getManager().getManagerTsid())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<PopupDTO> getSimilarPopup(long popupId) {

        List<Popup> popupList = popupRepository.findByEndDateGreaterThanEqualAndDeletedFalse(LocalDate.now());

        Popup currentPopup = popupRepository.findById(popupId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid popup ID"));

        List<PopupDTO> popupListRequest = new ArrayList<>();
        PopupDTO currentPopupRequest = PopupDTO.builder()
                .popupId(currentPopup.getPopupId())
                .name(currentPopup.getName())
                .content(currentPopup.getContent()).build();;

        for (Popup popup : popupList) {
            PopupDTO popupDTO = PopupDTO.builder()
                    .popupId(popup.getPopupId())
                    .name(popup.getName())
                    .content(popup.getContent()).build();

            popupListRequest.add(popupDTO);
        }

        String url = "http://70.12.130.111:9323/similar";
        SimilarPopupRequestDto requestDto = SimilarPopupRequestDto.builder()
                .currentPopupRequest(currentPopupRequest)
                .popupListRequest(popupListRequest).build();

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<SimilarPopupRequestDto> request = new HttpEntity<>(requestDto, headers);
        ResponseEntity<List<PopupDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<List<PopupDTO>>() {}
        );

        List<PopupDTO> responseList = response.getBody();
        List<PopupDTO> popupDtoList = new ArrayList<>();

        assert responseList != null;
        for (PopupDTO dto : responseList) {
            Popup popup = popupRepository.findById(dto.getPopupId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid popup ID"));

            List<PopupImage> imageList = popupImageRepository.findAllByPopup_PopupId(popup.getPopupId());
            List<String> imageUrl = new ArrayList<>();
            for (PopupImage image : imageList) {
                if (image.getSeq() != 1) continue;
                imageUrl.add(image.getImg());
            }
            PopupDTO popupDTO = PopupDTO.builder()
                    .popupId(popup.getPopupId())
                    .name(popup.getName())
                    .startDate(popup.getStartDate())
                    .endDate(popup.getEndDate())
                    .content(popup.getContent())
                    .images(imageUrl).build();

            popupDtoList.add(popupDTO);
        }

        return popupDtoList;
    }

    // 오픈 예정 팝업 조회
    public List<PopupDTO> getOpenPopup() {
        LocalDate now = LocalDate.now();
        List<Popup> popups = popupRepository.findAllByEndDateAfter(now);

        return popups.stream()
                .filter(popup -> !popup.isDeleted())
                .map(popup -> {
                    List<String> images = popupImageRepository.findAllByPopup_PopupId(popup.getPopupId())
                            .stream()
                            .sorted((img1, img2) -> Integer.compare(img1.getSeq(), img2.getSeq()))
                            .map(PopupImage::getImg)
                            .toList();
                    return PopupDTO.builder()
                            .popupId(popup.getPopupId())
                            .name(popup.getName())
                            .startDate(popup.getStartDate())
                            .endDate(popup.getEndDate())
                            .hours(popup.getHours())
                            .snsUrl(popup.getSnsUrl())
                            .pageUrl(popup.getPageUrl())
                            .content(popup.getContent())
                            .description(popup.getDescription())
                            .address(popup.getAddress())
                            .lat(popup.getLat())
                            .lon(popup.getLon())
                            .heart(popup.getHeart())
                            .hit(popup.getHit())
                            .rating(popup.getRating())
                            .managerTsId(popup.getManager().getManagerTsid())
                            .images(images)
                            .checkPreReservation(preReservationInfoRepository.existsByPopup(popup))
                            .build();
                })
                .collect(Collectors.toList());
    }

    // 사전 예약
    @Override
    @Transactional
    public PreReservationResponseDTO createPreReservation(PreReservationRequestDTO req) {
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

        PreReservation saved = preReservationRepository.save(preReservation);



        return convertToResponseDTO(saved);
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
    public PopupDTO createPopupOnly(PopupRequestDTO reqDto) {
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
                .address(reqDto.getAddress())
                .lat(reqDto.getLat())
                .lon(reqDto.getLon())
                .build();

        popupRepository.save(popup);

        List<String> images = reqDto.getImages().stream()
                .map(fileStorageService::storeFile)
                .toList();

        for (int i = 0; i < images.size(); i++) {
            PopupImage popupImage = PopupImage.builder()
                    .popup(popup)
                    .img(images.get(i))
                    .seq(i)
                    .build();
            popupImageRepository.save(popupImage);
        }

        List<PopupCategory> popupCategories = reqDto.getCategories().stream()
                .map(categoryId -> {
                    Category category = categoryRepository.findById(categoryId)
                            .orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));
                    return PopupCategory.builder()
                            .popup(popup)
                            .category(category)
                            .build();
                })
                .collect(Collectors.toList());

        popupCategoryRepository.saveAll(popupCategories);

        return PopupDTO.builder()
                .popupId(popup.getPopupId())
                .name(popup.getName())
                .startDate(popup.getStartDate())
                .endDate(popup.getEndDate())
                .hours(popup.getHours())
                .snsUrl(popup.getSnsUrl())
                .pageUrl(popup.getPageUrl())
                .content(popup.getContent())
                .description(popup.getDescription())
                .address(popup.getAddress())
                .lat(popup.getLat())
                .lon(popup.getLon())
                .heart(popup.getHeart())
                .hit(popup.getHit())
                .rating(popup.getRating())
                .managerTsId(popup.getManager().getManagerTsid())
                .images(images)
                .categories(popupCategories.stream().map(pc -> pc.getCategory().getName()).collect(Collectors.toList()))
                .build();
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
                .address(reqDto.getAddress())
                .lat(reqDto.getLat())
                .lon(reqDto.getLon())
                .build();

        popupRepository.save(popup);

        List<String> images = reqDto.getImages().stream()
                .map(fileStorageService::storeFile)
                .toList();

        for (int i = 0; i < images.size(); i++) {
            PopupImage popupImage = PopupImage.builder()
                    .popup(popup)
                    .img(images.get(i))
                    .seq(i)
                    .build();
            popupImageRepository.save(popupImage);
        }

        List<PopupCategory> popupCategories = reqDto.getCategories().stream()
                .map(categoryId -> {
                    Category category = categoryRepository.findById(categoryId)
                            .orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));
                    return PopupCategory.builder()
                            .popup(popup)
                            .category(category)
                            .build();
                })
                .collect(Collectors.toList());

        popupCategoryRepository.saveAll(popupCategories);

        // PreReservationInfo 엔티티 생성
        PreReservationInfo preReservationInfo = PreReservationInfo.builder()
                .popup(popup)
                .preReservationOpenAt(reqDto.getPreReservationOpenAt())
                .term(reqDto.getTerm())
                .maxPeoplePerSession(reqDto.getMaxPeoplePerSession())
                .maxReservationsPerPerson(reqDto.getMaxReservationsPerPerson())
                .warning(reqDto.getWarning())
                .build();

        preReservationInfoRepository.save(preReservationInfo);

    }

    // 팝업 수정
    @Transactional
    @Override
    public PopupDTO updatePopup(PopupRequestDTO reqDto, long popupId) {
        // 매니저 확인
        Manager manager = managerRepository.findByManagerTsid(reqDto.getManagerTsid())
                .orElseThrow(() -> new IllegalArgumentException("Invalid manager Tsid"));

        // 팝업 확인
        Popup popup = popupRepository.findById(popupId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid popup ID"));

        popup.updatePopup(reqDto);

        // 기존 이미지 삭제
        List<PopupImage> existingImages = popupImageRepository.findAllByPopup_PopupId(popupId);
        existingImages.forEach(image -> fileStorageService.deleteFile(image.getImg()));
        popupImageRepository.deleteAllByPopup(popup);

        // 새로운 이미지 저장
        List<String> images = reqDto.getImages().stream()
                .map(fileStorageService::storeFile)
                .toList();

        for (int i = 0; i < images.size(); i++) {
            PopupImage popupImage = PopupImage.builder()
                    .popup(popup)
                    .img(images.get(i))
                    .seq(i)
                    .build();
            popupImageRepository.save(popupImage);
        }

        // 기존 카테고리 삭제
        popupCategoryRepository.deleteAllByPopup(popup);

        // 카테고리 다시 저장
        List<PopupCategory> popupCategories = reqDto.getCategories().stream()
                .map(categoryId -> {
                    Category category = categoryRepository.findById(categoryId)
                            .orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));
                    return PopupCategory.builder()
                            .popup(popup)
                            .category(category)
                            .build();
                })
                .collect(Collectors.toList());

        popupCategoryRepository.saveAll(popupCategories);

        return PopupDTO.builder()
                .popupId(popup.getPopupId())
                .name(popup.getName())
                .startDate(popup.getStartDate())
                .endDate(popup.getEndDate())
                .hours(popup.getHours())
                .snsUrl(popup.getSnsUrl())
                .pageUrl(popup.getPageUrl())
                .content(popup.getContent())
                .description(popup.getDescription())
                .address(popup.getAddress())
                .lat(popup.getLat())
                .lon(popup.getLon())
                .heart(popup.getHeart())
                .hit(popup.getHit())
                .rating(popup.getRating())
                .managerTsId(popup.getManager().getManagerTsid())
                .images(images)
                .categories(popupCategories.stream().map(pc -> pc.getCategory().getName()).collect(Collectors.toList()))
                .build();
    }

    @Override
    @Transactional
    public void deletePopup(long popupId) {
        long managerTsid = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
        Popup findPopup = popupRepository.findById(popupId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.POPUP_NOT_FOUND));

        if(findPopup.isDeleted())

            throw new BusinessLogicException(ExceptionCode.POPUP_NOT_FOUND);

        if(findPopup.getManager().getManagerTsid() != managerTsid)
            throw new IllegalArgumentException("Not Valid Access");

        // 기존 이미지 삭제
        List<PopupImage> existingImages = popupImageRepository.findAllByPopup_PopupId(popupId);
        existingImages.forEach(image -> fileStorageService.deleteFile(image.getImg()));
        popupImageRepository.deleteAllByPopup(findPopup);

        findPopup.deletePopup();
    }

    // 본인이 등록한 팝업 목록 조회 (매니저)
    @Override
    public List<PopupDTO> getAllPopupByManager(Long managerTsId) {
        // 매니저 확인
        Manager manager = managerRepository.findByManagerTsid(managerTsId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid manager Tsid"));

        List<Popup> popups = popupRepository.findAllByManager(manager);

        return popups.stream()
                .filter(popup -> !popup.isDeleted())
                .map(popup -> {
                    List<String> images = popupImageRepository.findAllByPopup_PopupId(popup.getPopupId())
                            .stream()
                            .sorted((img1, img2) -> Integer.compare(img1.getSeq(), img2.getSeq()))
                            .map(PopupImage::getImg)
                            .toList();

                    List<String> categories = popupCategoryRepository.findByPopup(popup)
                            .stream()
                            .map(popupCategory -> popupCategory.getCategory().getName())
                            .toList();

                    return PopupDTO.builder()
                            .popupId(popup.getPopupId())
                            .name(popup.getName())
                            .startDate(popup.getStartDate())
                            .endDate(popup.getEndDate())
                            .hours(popup.getHours())
                            .snsUrl(popup.getSnsUrl())
                            .pageUrl(popup.getPageUrl())
                            .content(popup.getContent())
                            .description(popup.getDescription())
                            .address(popup.getAddress())
                            .lat(popup.getLat())
                            .lon(popup.getLon())
                            .heart(popup.getHeart())
                            .hit(popup.getHit())
                            .rating(popup.getRating())
                            .managerTsId(popup.getManager().getManagerTsid())
                            .images(images)
                            .categories(categories)
                            .checkPreReservation(preReservationInfoRepository.existsByPopup(popup))
                            .build();
                })
                .collect(Collectors.toList());
    }


    // 내 주변 팝업 조회 (전체)
    @Override
    public List<PopupDTO> getAllPopupByLocation() {
        LocalDate now = LocalDate.now();
        List<Popup> popups = popupRepository.findAllByEndDateAfter(now);

        return popups.stream()
                .filter(popup -> !popup.isDeleted())
                .map(popup -> {
                    List<String> images = popupImageRepository.findAllByPopup_PopupId(popup.getPopupId())
                            .stream()
                            .sorted((img1, img2) -> Integer.compare(img1.getSeq(), img2.getSeq()))
                            .map(PopupImage::getImg)
                            .toList();
                    return PopupDTO.builder()
                            .popupId(popup.getPopupId())
                            .name(popup.getName())
                            .startDate(popup.getStartDate())
                            .endDate(popup.getEndDate())
                            .hours(popup.getHours())
                            .snsUrl(popup.getSnsUrl())
                            .pageUrl(popup.getPageUrl())
                            .content(popup.getContent())
                            .description(popup.getDescription())
                            .address(popup.getAddress())
                            .lat(popup.getLat())
                            .lon(popup.getLon())
                            .heart(popup.getHeart())
                            .hit(popup.getHit())
                            .rating(popup.getRating())
                            .managerTsId(popup.getManager().getManagerTsid())
                            .images(images)
                            .checkPreReservation(preReservationInfoRepository.existsByPopup(popup))
                            .build();
                })
                .collect(Collectors.toList());
    }


    // 내 주변 팝업 조회 (좋아요)
    @Override
    public List<PopupDTO> getHeartPopupByLocation() {

        // 유저 확인
        long userTsid = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());

        User user = userRepository.findUserByUserTsid(userTsid)
                .orElseThrow(() -> new RuntimeException("user not exists"));

        LocalDate now = LocalDate.now();
        List<Popup> popups = popupRepository.findAllByEndDateAfter(now);

        List<Heart> hearts = heartRepository.findByUser(user);

        List<Popup> validHeartPopups = hearts.stream()
                .map(Heart::getPopup)
                .filter(popup -> popup.getEndDate().isAfter(now))
                .toList();

        return validHeartPopups.stream()
                .filter(popup -> !popup.isDeleted())
                .map(popup -> {
                    List<String> images = popupImageRepository.findAllByPopup_PopupId(popup.getPopupId())
                            .stream()
                            .sorted((img1, img2) -> Integer.compare(img1.getSeq(), img2.getSeq()))
                            .map(PopupImage::getImg)
                            .toList();

                    return PopupDTO.builder()
                            .popupId(popup.getPopupId())
                            .name(popup.getName())
                            .startDate(popup.getStartDate())
                            .endDate(popup.getEndDate())
                            .hours(popup.getHours())
                            .snsUrl(popup.getSnsUrl())
                            .pageUrl(popup.getPageUrl())
                            .content(popup.getContent())
                            .description(popup.getDescription())
                            .address(popup.getAddress())
                            .lat(popup.getLat())
                            .lon(popup.getLon())
                            .heart(popup.getHeart())
                            .hit(popup.getHit())
                            .rating(popup.getRating())
                            .managerTsId(popup.getManager().getManagerTsid())
                            .images(images)
                            .checkPreReservation(preReservationInfoRepository.existsByPopup(popup))
                            .build();
                })
                .collect(Collectors.toList());
    }


    // 내 주변 팝업 조회 (내 예약)
    @Override
    public List<PopupDTO> getMyReservationPopup() {

        // 유저 확인
        long userTsid = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());

        User user = userRepository.findUserByUserTsid(userTsid)
                .orElseThrow(() -> new RuntimeException("user not exists"));

        LocalDate now = LocalDate.now();
        List<Popup> popups = popupRepository.findAllByEndDateAfter(now);

        // 사용자가 예약한 OnsiteReservation 팝업 목록
        String phone = user.getPhoneNumber();
        List<Popup> onsitePopups = onsiteReservationRepository.findAllByPhoneNumber(phone)
                .stream()
                .map(OnsiteReservation::getPopup)
                .filter(popup -> popup.getEndDate().isAfter(now))
                .toList();

        // 사용자가 예약한 PreReservation 팝업 목록
        List<Popup> prePopups = preReservationRepository.findByUser(user)
                .stream()
                .map(PreReservation::getPopup)
                .filter(popup -> popup.getEndDate().isAfter(now))
                .toList();

        // 두 팝업 목록을 합치고 중복 제거
        List<Popup> allPopups = Stream.concat(onsitePopups.stream(), prePopups.stream())
                .distinct()
                .toList();

        return allPopups.stream()
                .filter(popup -> !popup.isDeleted())
                .map(popup -> {
                    List<String> images = popupImageRepository.findAllByPopup_PopupId(popup.getPopupId())
                            .stream()
                            .sorted((img1, img2) -> Integer.compare(img1.getSeq(), img2.getSeq()))
                            .map(PopupImage::getImg)
                            .toList();

                    return PopupDTO.builder()
                            .popupId(popup.getPopupId())
                            .name(popup.getName())
                            .startDate(popup.getStartDate())
                            .endDate(popup.getEndDate())
                            .hours(popup.getHours())
                            .snsUrl(popup.getSnsUrl())
                            .pageUrl(popup.getPageUrl())
                            .content(popup.getContent())
                            .description(popup.getDescription())
                            .address(popup.getAddress())
                            .lat(popup.getLat())
                            .lon(popup.getLon())
                            .heart(popup.getHeart())
                            .hit(popup.getHit())
                            .rating(popup.getRating())
                            .managerTsId(popup.getManager().getManagerTsid())
                            .images(images)
                            .checkPreReservation(preReservationInfoRepository.existsByPopup(popup))
                            .build();
                })
                .collect(Collectors.toList());

    }

    // 사전예약 유무 확인
    @Override
    public boolean checkPreReservation(long popupId) {
        Popup popup = popupRepository.findById(popupId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid popup ID"));

        if(popup.isDeleted())
            throw new BusinessLogicException(ExceptionCode.POPUP_NOT_FOUND);

        if(preReservationInfoRepository.existsByPopup(popup)) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    public List<PopupDTO> getPopupByCategory(String category) {
        List<Popup> list = popupRepository.findPopupsByCategoryName(category);

        List<PopupDTO> result = new ArrayList<>();
        for(Popup popup : list) {

            List<PopupImage> images = popupImageRepository.findAllByPopup_PopupId(popup.getPopupId());
            List<String> imageUrls = new ArrayList<>();
            for (PopupImage image : images) {
                imageUrls.add(image.getImg());
            }

            PopupDTO dto = PopupDTO.builder()
                    .popupId(popup.getPopupId())
                    .name(popup.getName())
                    .startDate(popup.getStartDate())
                    .endDate(popup.getEndDate())
                    .content(popup.getContent())
                    .heart(popup.getHeart())
                    .hit(popup.getHit())
                    .rating(popup.getRating())
                    .deleted(popup.isDeleted())
                    .images(imageUrls)
                    .build();

            result.add(dto);
        }
        return result;
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
