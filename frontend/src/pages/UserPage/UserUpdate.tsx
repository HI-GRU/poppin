import { useState } from "react";
import profile from "@assets/user/profile.png";

interface UserData {
  profileImage: string | null;
  nickname: string;
  email: string;
  phoneNumber: string;
  category: string[];
  marketing: boolean;
  push: boolean;
}

const UserUpdate = () => {
  const [userData, setUserData] = useState<UserData>({
    profileImage: null,
    nickname: "팝핑팝핑 팝프리카",
    email: "popping1234@kakao.com",
    phoneNumber: "010-1234-5678",
    category: [],
    marketing: false,
    push: false,
  });

  const userChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value, type, checked } = e.target;
    setUserData({
      ...userData,
      [name]: type === "checkbox" ? checked : value,
    });
  };

  const handleImageChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files[0]) {
      const file = e.target.files[0];
      const reader = new FileReader();
      reader.onloadend = () => {
        setUserData({
          ...userData,
          profileImage: reader.result as string,
        });
      };
      reader.readAsDataURL(file);
    }
  };

  const handleImageClick = () => {
    const fileInput = document.getElementById("profileImageInput");
    if (fileInput) {
      fileInput.click();
    }
  };

  const handleCategoryClick = (category: string) => {
    let newCategories = [...userData.category];
    if (newCategories.includes(category)) {
      newCategories = newCategories.filter((cat) => cat !== category);
    } else if (newCategories.length < 3) {
      newCategories.push(category);
    }
    setUserData({
      ...userData,
      category: newCategories,
    });
  };

  return (
    <div id="user-update">
      <div className="update-image">
        <div className="profile-black"></div>
        {userData.profileImage ? (
          <img
            src={userData.profileImage}
            alt="profile"
            onClick={handleImageClick}
          />
        ) : (
          <img src={profile} alt="profile" onClick={handleImageClick} />
        )}
        <input
          type="file"
          id="profileImageInput"
          style={{ display: "none" }}
          accept="image/*"
          onChange={handleImageChange}
        />
        <svg
          xmlns="http://www.w3.org/2000/svg"
          width="24"
          height="24"
          viewBox="0 0 24 24"
          fill="none"
        >
          <path
            d="M19.5 4.875H17.1019L15.9375 3.12563C15.8346 2.97138 15.6952 2.84497 15.5316 2.75763C15.368 2.67029 15.1854 2.62473 15 2.625H9C8.81458 2.62473 8.63197 2.67029 8.46841 2.75763C8.30484 2.84497 8.16541 2.97138 8.0625 3.12563L6.89719 4.875H4.5C3.80381 4.875 3.13613 5.15156 2.64384 5.64385C2.15156 6.13613 1.875 6.80381 1.875 7.5V18C1.875 18.6962 2.15156 19.3639 2.64384 19.8562C3.13613 20.3484 3.80381 20.625 4.5 20.625H19.5C19.8447 20.625 20.1861 20.5571 20.5045 20.4252C20.823 20.2933 21.1124 20.0999 21.3562 19.8562C21.5999 19.6124 21.7933 19.323 21.9252 19.0045C22.0571 18.6861 22.125 18.3447 22.125 18V7.5C22.125 7.15528 22.0571 6.81394 21.9252 6.49546C21.7933 6.17698 21.5999 5.8876 21.3562 5.64385C21.1124 5.40009 20.823 5.20674 20.5045 5.07482C20.1861 4.9429 19.8447 4.875 19.5 4.875ZM19.875 18C19.875 18.0995 19.8355 18.1948 19.7652 18.2652C19.6948 18.3355 19.5995 18.375 19.5 18.375H4.5C4.40054 18.375 4.30516 18.3355 4.23484 18.2652C4.16451 18.1948 4.125 18.0995 4.125 18V7.5C4.125 7.40055 4.16451 7.30516 4.23484 7.23484C4.30516 7.16451 4.40054 7.125 4.5 7.125H7.5C7.68542 7.12528 7.86803 7.07972 8.03159 6.99237C8.19516 6.90503 8.33459 6.77862 8.4375 6.62438L9.60187 4.875H14.3972L15.5625 6.62438C15.6654 6.77862 15.8048 6.90503 15.9684 6.99237C16.132 7.07972 16.3146 7.12528 16.5 7.125H19.5C19.5995 7.125 19.6948 7.16451 19.7652 7.23484C19.8355 7.30516 19.875 7.40055 19.875 7.5V18ZM12 7.875C11.11 7.875 10.24 8.13892 9.49993 8.63339C8.75991 9.12785 8.18314 9.83066 7.84254 10.6529C7.50195 11.4752 7.41283 12.38 7.58647 13.2529C7.7601 14.1258 8.18868 14.9276 8.81802 15.557C9.44736 16.1863 10.2492 16.6149 11.1221 16.7885C11.995 16.9622 12.8998 16.8731 13.7221 16.5325C14.5443 16.1919 15.2471 15.6151 15.7416 14.8751C16.2361 14.135 16.5 13.265 16.5 12.375C16.4988 11.1819 16.0243 10.038 15.1806 9.19439C14.337 8.35075 13.1931 7.87624 12 7.875ZM12 14.625C11.555 14.625 11.12 14.493 10.75 14.2458C10.38 13.9986 10.0916 13.6472 9.92127 13.236C9.75097 12.8249 9.70642 12.3725 9.79323 11.936C9.88005 11.4996 10.0943 11.0987 10.409 10.784C10.7237 10.4693 11.1246 10.2551 11.561 10.1682C11.9975 10.0814 12.4499 10.126 12.861 10.2963C13.2722 10.4666 13.6236 10.755 13.8708 11.125C14.118 11.495 14.25 11.93 14.25 12.375C14.25 12.9717 14.0129 13.544 13.591 13.966C13.169 14.3879 12.5967 14.625 12 14.625Z"
            fill="#D4145A"
          />
        </svg>
      </div>
      <div className="update-profile">
        <div className="update-nickname">
          <label htmlFor="nickname">닉네임</label>
          <input
            type="text"
            id="nickname"
            name="nickname"
            value={userData.nickname}
            onChange={userChange}
          />
          <button>중복확인</button>
        </div>
        <div className="update-email">
          <label htmlFor="email">게정 정보</label>
          <input
            type="text"
            id="email"
            name="email"
            value={userData.email}
            onChange={userChange}
          />
        </div>
        <div className="update-phone-number">
          <label htmlFor="phone-number">연락처</label>
          <input
            type="text"
            id="phone-number"
            name="phoneNumber"
            value={userData.phoneNumber}
            onChange={userChange}
          />
        </div>
        <div className="update-category">
          <h3>선호하는 카테고리</h3>
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
              onClick={() => handleCategoryClick(category)}
              className={userData.category.includes(category) ? "selected" : ""}
              disabled={
                userData.category.length >= 3 &&
                !userData.category.includes(category)
              }
            >
              {category}
            </button>
          ))}
        </div>
        <div className="update-agree">
          <h3>마케팅 수신 동의</h3>
          <div className="update-marketing">
            <input type="checkbox" id="marketing" name="marketing" />
            <label htmlFor="marketing">마케팅 수신 동의</label>
          </div>
          <p>(동의: 2024.07.24 15:32:08)</p>
          <div className="update-push">
            <input type="checkbox" id="push" name="push" />
            <label htmlFor="push">이벤트성 푸쉬 알림 수신 동의</label>
          </div>
          <p>(동의: 2024.07.24 15:32:08)</p>
        </div>
      </div>
    </div>
  );
};

export default UserUpdate;
