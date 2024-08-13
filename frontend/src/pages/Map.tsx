import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { PopupDetail } from "@interface/popDetail";
import {
  getMapPopupList,
  getMapHeartPopupByLocation,
  getMapMyReservationPopup,
} from "@api/apiPop";
import "@css/Map.css";

import openMarker from "@assets/map/openMarker.svg";
import upcomingMarker from "@assets/map/upcomingMarker.svg";
import likeMarker from "@assets/map/likeMarker.svg";
import reservationMarker from "@assets/map/reservationMarker.svg";
import currentLocation from "@assets/map/currentLocation.svg";
import useAuthStore from "@store/useAuthStore";

const markerImages = {
  open: openMarker,
  upcoming: upcomingMarker,
  like: likeMarker,
  reservation: reservationMarker,
};

interface PopupStore {
  position: kakao.maps.LatLng;
  image: string;
  title: string;
  schedule: string;
  businessHours: string;
  markerType: "open" | "upcoming" | "like" | "reservation";
  popupId: number;
}

const parseHours = (hoursString: string): Record<string, string> => {
  try {
    const parsedHours = JSON.parse(hoursString);
    return parsedHours;
  } catch (error) {
    console.error("Failed to parse hours:", error);
    return {};
  }
};

const getMarkerType = (
  popupDetail: PopupDetail
): "open" | "upcoming" | "like" | "reservation" => {
  const now = new Date();
  const currentTime = now.getHours() * 60 + now.getMinutes();
  const currentDay = ["일", "월", "화", "수", "목", "금", "토"][now.getDay()];

  const hours = parseHours(popupDetail.hours);
  const [openTimeStr, closeTimeStr] = hours[currentDay]
    ?.split("~")
    .map((str) => str.trim()) || ["00:00", "23:59"];
  const [openHour, openMinute] = openTimeStr.split(":").map(Number);
  const [closeHour, closeMinute] = closeTimeStr.split(":").map(Number);

  const openTime = openHour * 60 + openMinute;
  const closeTime = closeHour * 60 + closeMinute;

  const startDate = new Date(popupDetail.startDate);
  const endDate = new Date(popupDetail.endDate);

  if (
    now < startDate ||
    now > endDate ||
    currentTime < openTime ||
    currentTime > closeTime
  ) {
    return "upcoming";
  }

  return "open";
};

const convertToPopupStore = (popupDetail: PopupDetail): PopupStore => {
  const markerType = getMarkerType(popupDetail);
  return {
    position: new kakao.maps.LatLng(popupDetail.lat, popupDetail.lon),
    image: popupDetail.images[0],
    title: popupDetail.name,
    schedule: `${popupDetail.startDate} - ${popupDetail.endDate}`,
    businessHours: popupDetail.hours,
    markerType: markerType,
    popupId: popupDetail.popupId,
  };
};

const Map = () => {
  const [activeButton, setActiveButton] = useState<string>("all");
  const [selectedStore, setSelectedStore] = useState<PopupStore | null>(null);
  const [map, setMap] = useState<kakao.maps.Map | null>(null);
  const [markers, setMarkers] = useState<kakao.maps.Marker[]>([]);
  const [popupStores, setPopupStores] = useState<PopupStore[]>([]);
  const navigate = useNavigate();
  const isLoggedIn = useAuthStore((state) => !state.userTsid); // 로그인 여부 확인

  // 지도 초기화 및 마커 초기 설정
  useEffect(() => {
    const script = document.createElement("script");
    script.async = true;
    script.src =
      "//dapi.kakao.com/v2/maps/sdk.js?appkey=c306f7459b9ef7c16b36d8fea7076bb1&autoload=false";
    document.head.appendChild(script);

    script.onload = () => {
      kakao.maps.load(() => {
        const container = document.getElementById("map");
        if (container) {
          const options = {
            center: new kakao.maps.LatLng(37.54460323028253, 127.0560692732618),
            level: 3,
          };
          const map = new kakao.maps.Map(container, options);
          setMap(map);

          if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition((position) => {
              const lat = position.coords.latitude;
              const lon = position.coords.longitude;
              const locPosition = new kakao.maps.LatLng(lat, lon);

              map.setCenter(locPosition);

              const marker = new kakao.maps.Marker({
                map: map,
                position: locPosition,
              });

              marker.setMap(map);
            });
          }
        } else {
          console.error("Map container not found");
        }
      });
    };

    return () => {
      document.head.removeChild(script);
    };
  }, []);

  useEffect(() => {
    const fetchPopupStores = async () => {
      try {
        let data: PopupDetail[] = [];
        if (activeButton === "like" && isLoggedIn) {
          data = await getMapHeartPopupByLocation(); // 좋아요 API 호출
        } else if (activeButton === "reservation" && isLoggedIn) {
          data = await getMapMyReservationPopup(); // 사전예약 API 호출
        } else {
          data = await getMapPopupList(); // 전체 팝업 API 호출
        }

        const convertedStores = data.map(convertToPopupStore);
        setPopupStores(convertedStores);
      } catch (error) {
        console.error("Error fetching popup stores:", error);
      }
    };

    fetchPopupStores();
  }, [activeButton, isLoggedIn]);

  useEffect(() => {
    if (map) {
      // 마커 초기화
      markers.forEach((marker) => marker.setMap(null));

      const newMarkers = popupStores.map((store) => {
        const markerImage = new kakao.maps.MarkerImage(
          markerImages[store.markerType],
          new kakao.maps.Size(32, 32)
        );

        const marker = new kakao.maps.Marker({
          map: map,
          position: store.position,
          title: store.title,
          image: markerImage,
        });

        kakao.maps.event.addListener(marker, "click", () => {
          setSelectedStore(store);
        });

        return marker;
      });

      setMarkers(newMarkers);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [map, popupStores]);

  const handleButtonClick = (buttonType: string) => {
    setActiveButton(buttonType);
    setSelectedStore(null);
  };

  const handleCurrentLocationClick = () => {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition((position) => {
        const lat = position.coords.latitude;
        const lon = position.coords.longitude;
        const locPosition = new kakao.maps.LatLng(lat, lon);

        if (map) {
          map.setCenter(locPosition);
        }
      });
    }
  };

  const handleMapClick = () => {
    setSelectedStore(null);
  };

  useEffect(() => {
    if (map) {
      kakao.maps.event.addListener(map, "click", handleMapClick);
    }

    return () => {
      if (map) {
        kakao.maps.event.removeListener(map, "click", handleMapClick);
      }
    };
  }, [map]);

  const isOpen = (store: PopupStore) => {
    const now = new Date();
    const currentMinutes = now.getHours() * 60 + now.getMinutes();

    const [startDate, endDate] = store.schedule
      .split(" - ")
      .map((dateStr) => new Date(dateStr.replace(/\./g, "-")));

    const hours = store.businessHours.split(" - ");
    const [openHour, openMinute] = hours[0].split(":").map(Number);
    const [closeHour, closeMinute] = hours[1].split(":").map(Number);

    const openMinutes = openHour * 60 + openMinute;
    const closeMinutes = closeHour * 60 + closeMinute;

    const isWithinDateRange = now >= startDate && now <= endDate;
    const isWithinTimeRange =
      currentMinutes >= openMinutes && currentMinutes <= closeMinutes;

    return isWithinDateRange && isWithinTimeRange;
  };

  const handleStoreClick = () => {
    if (selectedStore) {
      navigate(`/popupDetail/${selectedStore.popupId}`);
    }
  };

  return (
    <div id="mappage">
      <div className="title">내 주변 팝업</div>
      <div className="buttons">
        <button
          className={`button ${activeButton === "all" ? "active" : ""}`}
          onClick={() => handleButtonClick("all")}
        >
          전체
        </button>
        {isLoggedIn && (
          <>
            <button
              className={`button ${activeButton === "like" ? "active" : ""}`}
              onClick={() => handleButtonClick("like")}
            >
              좋아요
            </button>
            <button
              className={`button ${activeButton === "reservation" ? "active" : ""}`}
              onClick={() => handleButtonClick("reservation")}
            >
              내 예약
            </button>
          </>
        )}
      </div>
      <div id="map" className="map"></div>
      <div>
        <button
          className="current-location"
          onClick={handleCurrentLocationClick}
          style={{ bottom: selectedStore ? "130px" : "20px" }}
        >
          <img src={currentLocation} />
        </button>
        <div className="info">
          {selectedStore && (
            <div className="store-info" onClick={handleStoreClick}>
              <img src={selectedStore.image} alt={selectedStore.title} />
              <div className="store-details">
                <h2>{selectedStore.title}</h2>
                <p>{selectedStore.schedule}</p>
                <p className={isOpen(selectedStore) ? "open" : "upcoming"}>
                  {isOpen(selectedStore) ? "영업중" : "오픈 예정"}
                </p>
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default Map;
