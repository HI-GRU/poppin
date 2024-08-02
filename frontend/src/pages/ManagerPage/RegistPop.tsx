import React, { useState } from "react";
import DatePicker, { registerLocale } from "react-datepicker";
import DaumPostcodeEmbed, { Address } from "react-daum-postcode";
import { ko } from "date-fns/locale";
import { useNavigate } from "react-router-dom";
import usePopupStore from "store/usePopupStore";

import "@css/ManagerPage/RegistPop.css";
import "react-datepicker/dist/react-datepicker.css";

import registPhoto from "@assets/registPop/registPhoto.svg";
import firstStep from "@assets/registPop/firstStep.svg";

interface HeaderProps {
  date: Date;
  decreaseMonth: () => void;
  increaseMonth: () => void;
  prevMonthButtonDisabled: boolean;
  nextMonthButtonDisabled: boolean;
}

interface KakaoLatLng {
  lat: number;
  lng: number;
}

registerLocale("ko", ko);

function RegistPop() {
  const {
    storeName,
    setStoreName,
    storeDescription,
    setStoreDescription,
    selectedImages,
    setSelectedImages,
    setPopupData,
    startDate,
    setStartDate,
    endDate,
    setEndDate,
    address,
    setAddress,
    detailedAddress,
    setDetailedAddress,
    lat,
    setLat,
    lon,
    setLon,
    setHours,
    setCategories,
  } = usePopupStore();
  const [selectedDays, setSelectedDays] = useState<string[]>([]);
  const [timeSlots, setTimeSlots] = useState<
    { day: string; startTime: string; endTime: string }[]
  >([]);
  const [selectedStartTimeHour, setSelectedStartTimeHour] =
    useState<string>("");
  const [selectedStartTimeMinute, setSelectedStartTimeMinute] =
    useState<string>("");
  const [selectedEndTimeHour, setSelectedEndTimeHour] = useState<string>("");
  const [selectedEndTimeMinute, setSelectedEndTimeMinute] =
    useState<string>("");
  const [selectedCategories, setSelectedCategories] = useState<string[]>([]);
  const [showAddressModal, setShowAddressModal] = useState(false);
  const navigate = useNavigate();

  const goNextStep = () => {
    if (!startDate || !endDate || lat === null || lon === null) {
      console.log(startDate, endDate, lat, lon);
      alert("시작일과 종료일, 그리고 주소를 모두 선택하세요.");
      return;
    }

    const hours = timeSlots
      .map((slot) => `${slot.day}, ${slot.startTime}, ${slot.endTime}`)
      .join("\n");

    setHours(hours);
    setCategories(selectedCategories);

    setPopupData({
      storeName,
      storeDescription,
      selectedImages,
      startDate,
      endDate,
      hours,
      address,
      detailedAddress,
      lat,
      lon,
      categories: selectedCategories,
    });
    console.log(
      storeName,
      storeDescription,
      selectedImages,
      startDate,
      endDate,
      hours,
      address,
      detailedAddress,
      lat,
      lon,
      selectedCategories
    );

    navigate("/regist-pop-optional");
  };

  const handleImageChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    if (event.target.files) {
      const files = Array.from(event.target.files);
      const promises = files.map((file) => {
        return new Promise<string>((resolve, reject) => {
          const reader = new FileReader();
          reader.readAsDataURL(file);
          reader.onload = () => resolve(reader.result as string);
          reader.onerror = (error) => reject(error);
        });
      });

      Promise.all(promises)
        .then((base64Files) => {
          setSelectedImages([...selectedImages, ...base64Files]);
        })
        .catch((error) => {
          console.error("Error reading files: ", error);
        });
    }
  };

  const handleDayChange = (event: React.ChangeEvent<HTMLSelectElement>) => {
    setSelectedDays([...selectedDays, event.target.value]);
  };

  const handleAddressComplete = async (data: Address): Promise<KakaoLatLng> => {
    setAddress(data.address);
    setShowAddressModal(false);

    const geocoder = new kakao.maps.services.Geocoder();
    return await new Promise((resolve, reject) => {
      geocoder.addressSearch(data.address, (result, status) => {
        if (status === kakao.maps.services.Status.OK) {
          const coords = result[0];
          const lat = parseFloat(coords.y);
          const lng = parseFloat(coords.x);
          setLat(lat);
          setLon(lng);
          resolve({ lat, lng });
        } else {
          reject(new Error("Geocoding failed"));
        }
      });
    });
  };

  const handleCategoryClick = (category: string) => {
    return (event: React.MouseEvent<HTMLButtonElement>) => {
      event.preventDefault();
      let newCategories = [...selectedCategories];
      if (newCategories.includes(category)) {
        newCategories = newCategories.filter((cat) => cat !== category);
      } else if (newCategories.length < 3) {
        newCategories.push(category);
      }
      setSelectedCategories(newCategories);
    };
  };

  const handleAddTimeSlot = () => {
    const startTime = `${selectedStartTimeHour}:${selectedStartTimeMinute}`;
    const endTime = `${selectedEndTimeHour}:${selectedEndTimeMinute}`;

    if (
      selectedDays.length > 0 &&
      selectedStartTimeHour &&
      selectedStartTimeMinute &&
      selectedEndTimeHour &&
      selectedEndTimeMinute
    ) {
      setTimeSlots([
        ...timeSlots,
        { day: selectedDays[selectedDays.length - 1], startTime, endTime },
      ]);
      // setSelectedStartTimeHour("");
      // setSelectedStartTimeMinute("");
      // setSelectedEndTimeHour("");
      // setSelectedEndTimeMinute("");
    }
  };

  const renderCustomHeader = ({
    date,
    decreaseMonth,
    increaseMonth,
    prevMonthButtonDisabled,
    nextMonthButtonDisabled,
  }: HeaderProps) => (
    <div className="custom-header">
      <button onClick={decreaseMonth} disabled={prevMonthButtonDisabled}>
        {"<"}
      </button>
      <span className="custom-header__date">
        {date.toLocaleString("ko", { month: "long" })}
      </span>
      <button onClick={increaseMonth} disabled={nextMonthButtonDisabled}>
        {">"}
      </button>
    </div>
  );

  const hourOptions = Array.from({ length: 24 }, (_, i) => (
    <option key={i} value={i < 10 ? `0${i}` : i}>
      {i < 10 ? `0${i}` : i}
    </option>
  ));

  const minuteOptions = Array.from({ length: 60 }, (_, i) => (
    <option key={i} value={i < 10 ? `0${i}` : i}>
      {i < 10 ? `0${i}` : i}
    </option>
  ));

  return (
    <div id="regist-pop">
      <div className="essential-info-title">팝업스토어 필수 정보</div>
      <div className="step-one">
        <img src={firstStep} />
      </div>
      <form>
        <div className="regist-photo-content">
          <label>팝업스토어 사진</label>
          <label htmlFor="store-images" className="regist-photo">
            <img src={registPhoto} />
            팝업스토어 사진을 등록하세요.(최대10장)
          </label>
          <input
            type="file"
            id="store-images"
            accept="image/*"
            multiple
            onChange={handleImageChange}
          />
          <div className="image-preview">
            {selectedImages.map((image, index) => (
              <img key={index} src={image} alt={`popup-store-${index}`} />
            ))}
          </div>
        </div>
        <div className="regist-popup-name">
          <label>팝업스토어 이름</label>
          <input
            placeholder="팝업스토어 이름을 입력하세요."
            value={storeName}
            onChange={(e) => setStoreName(e.target.value)}
          />
        </div>
        <div>
          <label>팝업스토어 운영 일정</label>
          <div className="date-term">
            <DatePicker
              className="date-term-input"
              selected={startDate}
              onChange={(date) => setStartDate(date)}
              placeholderText="운영 시작일"
              dateFormat="Y. M. d."
              locale="ko"
              renderCustomHeader={renderCustomHeader}
            />
            <div className="wave"> ~ </div>
            <DatePicker
              className="date-term-input"
              selected={endDate}
              onChange={(date) => setEndDate(date)}
              placeholderText="운영 종료일"
              dateFormat="Y. M. d."
              locale="ko"
              renderCustomHeader={renderCustomHeader}
            />
          </div>
        </div>
        <div>
          <label>팝업스토어 운영 시간</label>
          <div className="inline">
            <select onChange={handleDayChange}>
              <option value="">-</option>
              <option value="월">월</option>
              <option value="화">화</option>
              <option value="수">수</option>
              <option value="목">목</option>
              <option value="금">금</option>
              <option value="토">토</option>
              <option value="일">일</option>
            </select>
            <div className="time-input-group">
              <select
                value={selectedStartTimeHour}
                onChange={(e) => setSelectedStartTimeHour(e.target.value)}
                className="time-picker"
              >
                <option value="">--</option>
                {hourOptions}
              </select>
              <span className="time-colon">:</span>
              <select
                value={selectedStartTimeMinute}
                onChange={(e) => setSelectedStartTimeMinute(e.target.value)}
                className="time-picker"
              >
                <option value="">--</option>
                {minuteOptions}
              </select>
            </div>
            <span className="time-separator">~</span>
            <div className="time-input-group">
              <select
                value={selectedEndTimeHour}
                onChange={(e) => setSelectedEndTimeHour(e.target.value)}
                className="time-picker"
              >
                <option value="">--</option>
                {hourOptions}
              </select>
              <span className="time-colon">:</span>
              <select
                value={selectedEndTimeMinute}
                onChange={(e) => setSelectedEndTimeMinute(e.target.value)}
                className="time-picker"
              >
                <option value="">--</option>
                {minuteOptions}
              </select>
            </div>
            <button type="button" onClick={handleAddTimeSlot}>
              +
            </button>
          </div>
          <div className="added-time">
            {timeSlots.map((slot, index) => (
              <div key={index} className="added-time-detail">
                {slot.day} - {slot.startTime} ~ {slot.endTime}
              </div>
            ))}
          </div>
        </div>
        <div>
          <label>주소 검색하기</label>
          <label
            className="address-search"
            onClick={() => setShowAddressModal(true)}
          >
            주소를 검색해주세요.
          </label>
          <div className={`address-modal ${showAddressModal ? "active" : ""}`}>
            <DaumPostcodeEmbed onComplete={handleAddressComplete} />
          </div>
          <div>
            <input
              className="detail-address"
              placeholder="상세 주소를 입력해주세요."
              value={detailedAddress}
              onChange={(e) => setDetailedAddress(e.target.value)}
            />
          </div>
          <div className="address-container">
            <div className="added-address">{`${address} ${detailedAddress}`}</div>
          </div>
        </div>
        <div>
          <label>팝업스토어 소개 및 설명</label>
          <textarea
            placeholder="팝업스토어에 대한 소개 및 설명을 입력하세요"
            value={storeDescription}
            onChange={(e) => setStoreDescription(e.target.value)}
          />
        </div>
        <div>
          <label>팝업스토어 카테고리 (최대 3개)</label>
          <div className="category-buttons">
            {[
              "뷰티",
              "패션",
              "음식/음료",
              "캐릭터",
              "리빙/금융",
              "연예",
              "게임",
              "가전/디지털",
              "콘텐츠",
              "취미/여가",
            ].map((category) => (
              <button
                key={category}
                type="button"
                onClick={handleCategoryClick(category)}
                className={
                  selectedCategories.includes(category) ? "selected" : ""
                }
                disabled={
                  selectedCategories.length >= 3 &&
                  !selectedCategories.includes(category)
                }
              >
                {category}
              </button>
            ))}
          </div>
        </div>
        <div className="go-next">
          <button type="button" onClick={goNextStep}>
            다음 단계 진행
          </button>
        </div>
      </form>
    </div>
  );
}

export default RegistPop;
