package com.apink.poppin.api.popup.repository;

import com.apink.poppin.api.popup.entity.Popup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PopupRepository extends JpaRepository<Popup, Long> {

    // 전체 목록 조회
    List<Popup> findAll();

}
