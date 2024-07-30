import "@css/Mypage.css";
import { Link } from "react-router-dom";

const Mypage = () => {
  return (
    <div id="my-page">
      <div className="login-section">
        {/* 로그인 전 */}
        <Link to="/login">
          <svg
            className="login-before"
            xmlns="http://www.w3.org/2000/svg"
            width="48"
            height="48"
            viewBox="0 0 48 48"
            fill="none"
          >
            <path
              d="M21.5 23.25V8.25C21.5 7.65326 21.7371 7.08097 22.159 6.65901C22.581 6.23705 23.1533 6 23.75 6C24.3467 6 24.919 6.23705 25.341 6.65901C25.7629 7.08097 26 7.65326 26 8.25V23.25C26 23.8467 25.7629 24.419 25.341 24.841C24.919 25.2629 24.3467 25.5 23.75 25.5C23.1533 25.5 22.581 25.2629 22.159 24.841C21.7371 24.419 21.5 23.8467 21.5 23.25ZM33.9781 7.875C33.4785 7.56737 32.8785 7.46707 32.306 7.59548C31.7335 7.72389 31.2338 8.07085 30.9134 8.5624C30.5931 9.05395 30.4774 9.65119 30.591 10.2268C30.7047 10.8024 31.0387 11.3109 31.5219 11.6437C35.6394 14.3194 38 18.5625 38 23.25C38 27.0293 36.4987 30.6539 33.8263 33.3263C31.1539 35.9987 27.5293 37.5 23.75 37.5C19.9707 37.5 16.3461 35.9987 13.6737 33.3263C11.0013 30.6539 9.5 27.0293 9.5 23.25C9.5 18.5625 11.8606 14.3194 15.9781 11.6344C16.4356 11.2915 16.7454 10.7874 16.8446 10.2244C16.9438 9.66145 16.825 9.08181 16.5123 8.60324C16.1996 8.12467 15.7165 7.78307 15.1611 7.64781C14.6057 7.51255 14.0196 7.59378 13.5219 7.875C8.105 11.3963 5 17.0044 5 23.25C5 28.2228 6.97544 32.9919 10.4917 36.5083C14.0081 40.0246 18.7772 42 23.75 42C28.7228 42 33.4919 40.0246 37.0083 36.5083C40.5246 32.9919 42.5 28.2228 42.5 23.25C42.5 17.0044 39.395 11.3963 33.9781 7.875Z"
              fill="#D4145A"
            />
          </svg>
          <span>로그인 해주세요</span>
          <svg
            xmlns="http://www.w3.org/2000/svg"
            width="16"
            height="16"
            viewBox="0 0 16 16"
            fill="none"
          >
            <path
              d="M11.354 8.35375L6.35403 13.3538C6.30757 13.4002 6.25242 13.4371 6.19173 13.4622C6.13103 13.4873 6.06598 13.5003 6.00028 13.5003C5.93458 13.5003 5.86953 13.4873 5.80883 13.4622C5.74813 13.4371 5.69298 13.4002 5.64653 13.3538C5.60007 13.3073 5.56322 13.2521 5.53808 13.1915C5.51294 13.1308 5.5 13.0657 5.5 13C5.5 12.9343 5.51294 12.8693 5.53808 12.8086C5.56322 12.7479 5.60007 12.6927 5.64653 12.6463L10.2934 8L5.64653 3.35375C5.55271 3.25993 5.5 3.13269 5.5 3C5.5 2.86732 5.55271 2.74007 5.64653 2.64625C5.74035 2.55243 5.8676 2.49973 6.00028 2.49973C6.13296 2.49973 6.26021 2.55243 6.35403 2.64625L11.354 7.64625C11.4005 7.69269 11.4374 7.74783 11.4626 7.80853C11.4877 7.86923 11.5007 7.9343 11.5007 8C11.5007 8.06571 11.4877 8.13077 11.4626 8.19147C11.4374 8.25217 11.4005 8.30732 11.354 8.35375Z"
              fill="black"
            />
          </svg>
        </Link>
      </div>
      <div className="login-section">
        {/* 로그인 후 */}
        <div className="login-wrap">
          <div className="mypage-profile">
            <div className="mypage-profile-image">
              <img
                src="https://i.pinimg.com/564x/ac/53/e9/ac53e9b1cbb1069713a4b8b78986b5cd.jpg"
                alt="프로필 사진"
              />
            </div>
            <span className="mypage-nickname">팝팝 터지는 커비</span>님
            <Link to="/mypage/update" className="profile-update">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                width="16"
                height="16"
                viewBox="0 0 16 16"
                fill="none"
              >
                <path
                  d="M8.00012 5.00001C7.40677 5.00001 6.82676 5.17596 6.33341 5.5056C5.84006 5.83524 5.45554 6.30378 5.22848 6.85196C5.00142 7.40014 4.94201 8.00334 5.05776 8.58528C5.17352 9.16722 5.45924 9.70177 5.8788 10.1213C6.29836 10.5409 6.8329 10.8266 7.41485 10.9424C7.99679 11.0581 8.59999 10.9987 9.14817 10.7716C9.69635 10.5446 10.1649 10.1601 10.4945 9.66672C10.8242 9.17337 11.0001 8.59335 11.0001 8.00001C10.9993 7.20461 10.683 6.44203 10.1205 5.8796C9.5581 5.31717 8.79551 5.00084 8.00012 5.00001ZM8.00012 10C7.60456 10 7.21788 9.88271 6.88898 9.66295C6.56008 9.44318 6.30373 9.13083 6.15236 8.76538C6.00098 8.39992 5.96138 7.99779 6.03855 7.60983C6.11572 7.22187 6.3062 6.8655 6.58591 6.58579C6.86561 6.30609 7.22198 6.11561 7.60994 6.03844C7.9979 5.96127 8.40003 6.00087 8.76549 6.15225C9.13094 6.30362 9.4433 6.55997 9.66306 6.88887C9.88282 7.21777 10.0001 7.60445 10.0001 8.00001C10.0001 8.53044 9.78941 9.03915 9.41433 9.41422C9.03926 9.78929 8.53055 10 8.00012 10ZM14.8714 6.70063C14.8574 6.63022 14.8285 6.56364 14.7866 6.5054C14.7446 6.44717 14.6906 6.39864 14.6282 6.36313L12.7639 5.30063L12.7564 3.19938C12.7561 3.12702 12.7402 3.05556 12.7097 2.98995C12.6791 2.92435 12.6347 2.86615 12.5795 2.81938C11.9032 2.24732 11.1244 1.80892 10.2845 1.52751C10.2184 1.50512 10.1483 1.49684 10.0787 1.50319C10.0092 1.50954 9.94174 1.53038 9.88074 1.56438L8.00012 2.61563L6.11762 1.56251C6.05659 1.52832 5.98906 1.50731 5.91941 1.50085C5.84975 1.4944 5.77952 1.50262 5.71324 1.52501C4.87396 1.80848 4.09605 2.24855 3.42074 2.82188C3.3656 2.86858 3.32123 2.92668 3.2907 2.99217C3.26016 3.05766 3.24419 3.129 3.24387 3.20126L3.23449 5.30438L1.37012 6.36688C1.30774 6.40239 1.25375 6.45092 1.21179 6.50915C1.16984 6.56739 1.14092 6.63397 1.12699 6.70438C0.956367 7.56181 0.956367 8.44446 1.12699 9.30188C1.14092 9.37229 1.16984 9.43888 1.21179 9.49712C1.25375 9.55535 1.30774 9.60387 1.37012 9.63938L3.23449 10.7019L3.24199 12.8031C3.24222 12.8755 3.25815 12.947 3.28869 13.0126C3.31923 13.0782 3.36364 13.1364 3.41887 13.1831C4.09516 13.7552 4.87397 14.1936 5.71387 14.475C5.78001 14.4974 5.85011 14.5057 5.91965 14.4993C5.98919 14.493 6.05663 14.4721 6.11762 14.4381L8.00012 13.3844L9.88262 14.4375C9.95712 14.479 10.0411 14.5005 10.1264 14.5C10.181 14.5 10.2352 14.4911 10.287 14.4738C11.1262 14.1907 11.9041 13.751 12.5795 13.1781C12.6346 13.1314 12.679 13.0733 12.7095 13.0078C12.7401 12.9424 12.7561 12.871 12.7564 12.7988L12.7657 10.6956L14.6301 9.63313C14.6925 9.59762 14.7465 9.5491 14.7884 9.49087C14.8304 9.43263 14.8593 9.36604 14.8732 9.29563C15.0429 8.43889 15.0423 7.55713 14.8714 6.70063ZM13.9339 8.88251L12.1482 9.89813C12.07 9.94262 12.0052 10.0074 11.9607 10.0856C11.9245 10.1481 11.8864 10.2144 11.8476 10.2769C11.798 10.3557 11.7716 10.4469 11.7714 10.54L11.762 12.5556C11.282 12.9325 10.7473 13.2339 10.1764 13.4494L8.37512 12.4456C8.30034 12.4043 8.2162 12.3827 8.13074 12.3831H8.11887C8.04324 12.3831 7.96699 12.3831 7.89137 12.3831C7.80192 12.3809 7.71349 12.4025 7.63512 12.4456L5.83262 13.4519C5.26044 13.2381 4.72426 12.9382 4.24262 12.5625L4.23574 10.55C4.23544 10.4567 4.20902 10.3653 4.15949 10.2863C4.12074 10.2238 4.08262 10.1613 4.04699 10.095C4.00282 10.0156 3.93805 9.9495 3.85949 9.90376L2.07199 8.88563C1.97949 8.30052 1.97949 7.7045 2.07199 7.11938L3.85449 6.10188C3.93273 6.0574 3.99751 5.99262 4.04199 5.91438C4.07824 5.85188 4.11637 5.78563 4.15512 5.72313C4.20472 5.64432 4.23114 5.55313 4.23137 5.46001L4.24074 3.44438C4.7207 3.06747 5.25541 2.76608 5.82637 2.55063L7.62512 3.55438C7.7034 3.59778 7.7919 3.61936 7.88137 3.61688C7.95699 3.61688 8.03324 3.61688 8.10887 3.61688C8.19831 3.61912 8.28675 3.59755 8.36512 3.55438L10.1676 2.54813C10.7398 2.76193 11.276 3.06184 11.7576 3.43751L11.7645 5.45001C11.7648 5.54332 11.7912 5.63468 11.8407 5.71376C11.8795 5.77626 11.9176 5.83876 11.9532 5.90501C11.9974 5.98445 12.0622 6.05052 12.1407 6.09626L13.9282 7.11438C14.022 7.69995 14.023 8.29661 13.9314 8.88251H13.9339Z"
                  fill="#AAAAAA"
                />
              </svg>
            </Link>
          </div>
          <Link to="" className="logout">
            로그아웃
          </Link>
        </div>
      </div>
      <div className="mypage-menu-section">
        <ul>
          <li>
            <Link to="/mypage/review">
              <p>내가 작성한 팝업 후기</p>
              <svg
                xmlns="http://www.w3.org/2000/svg"
                width="16"
                height="16"
                viewBox="0 0 16 16"
                fill="none"
              >
                <path
                  d="M11.354 8.35375L6.35403 13.3538C6.30757 13.4002 6.25242 13.4371 6.19173 13.4622C6.13103 13.4873 6.06598 13.5003 6.00028 13.5003C5.93458 13.5003 5.86953 13.4873 5.80883 13.4622C5.74813 13.4371 5.69298 13.4002 5.64653 13.3538C5.60007 13.3073 5.56322 13.2521 5.53808 13.1915C5.51294 13.1308 5.5 13.0657 5.5 13C5.5 12.9343 5.51294 12.8693 5.53808 12.8086C5.56322 12.7479 5.60007 12.6927 5.64653 12.6463L10.2934 8L5.64653 3.35375C5.55271 3.25993 5.5 3.13269 5.5 3C5.5 2.86732 5.55271 2.74007 5.64653 2.64625C5.74035 2.55243 5.8676 2.49973 6.00028 2.49973C6.13296 2.49973 6.26021 2.55243 6.35403 2.64625L11.354 7.64625C11.4005 7.69269 11.4374 7.74783 11.4626 7.80853C11.4877 7.86923 11.5007 7.9343 11.5007 8C11.5007 8.06571 11.4877 8.13077 11.4626 8.19147C11.4374 8.25217 11.4005 8.30732 11.354 8.35375Z"
                  fill="black"
                />
              </svg>
            </Link>
          </li>
          <li>
            <Link to="/mypage/like">
              <p>내가 좋아요한 팝업</p>
              <svg
                xmlns="http://www.w3.org/2000/svg"
                width="16"
                height="16"
                viewBox="0 0 16 16"
                fill="none"
              >
                <path
                  d="M11.354 8.35375L6.35403 13.3538C6.30757 13.4002 6.25242 13.4371 6.19173 13.4622C6.13103 13.4873 6.06598 13.5003 6.00028 13.5003C5.93458 13.5003 5.86953 13.4873 5.80883 13.4622C5.74813 13.4371 5.69298 13.4002 5.64653 13.3538C5.60007 13.3073 5.56322 13.2521 5.53808 13.1915C5.51294 13.1308 5.5 13.0657 5.5 13C5.5 12.9343 5.51294 12.8693 5.53808 12.8086C5.56322 12.7479 5.60007 12.6927 5.64653 12.6463L10.2934 8L5.64653 3.35375C5.55271 3.25993 5.5 3.13269 5.5 3C5.5 2.86732 5.55271 2.74007 5.64653 2.64625C5.74035 2.55243 5.8676 2.49973 6.00028 2.49973C6.13296 2.49973 6.26021 2.55243 6.35403 2.64625L11.354 7.64625C11.4005 7.69269 11.4374 7.74783 11.4626 7.80853C11.4877 7.86923 11.5007 7.9343 11.5007 8C11.5007 8.06571 11.4877 8.13077 11.4626 8.19147C11.4374 8.25217 11.4005 8.30732 11.354 8.35375Z"
                  fill="black"
                />
              </svg>
            </Link>
          </li>
          <li>
            <Link to="/mypage/reservation">
              <p>내가 예약한 팝업</p>
              <svg
                xmlns="http://www.w3.org/2000/svg"
                width="16"
                height="16"
                viewBox="0 0 16 16"
                fill="none"
              >
                <path
                  d="M11.354 8.35375L6.35403 13.3538C6.30757 13.4002 6.25242 13.4371 6.19173 13.4622C6.13103 13.4873 6.06598 13.5003 6.00028 13.5003C5.93458 13.5003 5.86953 13.4873 5.80883 13.4622C5.74813 13.4371 5.69298 13.4002 5.64653 13.3538C5.60007 13.3073 5.56322 13.2521 5.53808 13.1915C5.51294 13.1308 5.5 13.0657 5.5 13C5.5 12.9343 5.51294 12.8693 5.53808 12.8086C5.56322 12.7479 5.60007 12.6927 5.64653 12.6463L10.2934 8L5.64653 3.35375C5.55271 3.25993 5.5 3.13269 5.5 3C5.5 2.86732 5.55271 2.74007 5.64653 2.64625C5.74035 2.55243 5.8676 2.49973 6.00028 2.49973C6.13296 2.49973 6.26021 2.55243 6.35403 2.64625L11.354 7.64625C11.4005 7.69269 11.4374 7.74783 11.4626 7.80853C11.4877 7.86923 11.5007 7.9343 11.5007 8C11.5007 8.06571 11.4877 8.13077 11.4626 8.19147C11.4374 8.25217 11.4005 8.30732 11.354 8.35375Z"
                  fill="black"
                />
              </svg>
            </Link>
          </li>
          <li>
            <Link to="">
              <p>예약이 취소된 팝업</p>
              <svg
                xmlns="http://www.w3.org/2000/svg"
                width="16"
                height="16"
                viewBox="0 0 16 16"
                fill="none"
              >
                <path
                  d="M11.354 8.35375L6.35403 13.3538C6.30757 13.4002 6.25242 13.4371 6.19173 13.4622C6.13103 13.4873 6.06598 13.5003 6.00028 13.5003C5.93458 13.5003 5.86953 13.4873 5.80883 13.4622C5.74813 13.4371 5.69298 13.4002 5.64653 13.3538C5.60007 13.3073 5.56322 13.2521 5.53808 13.1915C5.51294 13.1308 5.5 13.0657 5.5 13C5.5 12.9343 5.51294 12.8693 5.53808 12.8086C5.56322 12.7479 5.60007 12.6927 5.64653 12.6463L10.2934 8L5.64653 3.35375C5.55271 3.25993 5.5 3.13269 5.5 3C5.5 2.86732 5.55271 2.74007 5.64653 2.64625C5.74035 2.55243 5.8676 2.49973 6.00028 2.49973C6.13296 2.49973 6.26021 2.55243 6.35403 2.64625L11.354 7.64625C11.4005 7.69269 11.4374 7.74783 11.4626 7.80853C11.4877 7.86923 11.5007 7.9343 11.5007 8C11.5007 8.06571 11.4877 8.13077 11.4626 8.19147C11.4374 8.25217 11.4005 8.30732 11.354 8.35375Z"
                  fill="black"
                />
              </svg>
            </Link>
          </li>
        </ul>
      </div>
      <div className="notice-section">
        <ul>
          <li>
            <Link to="">
              <p>공지사항</p>
              <svg
                xmlns="http://www.w3.org/2000/svg"
                width="16"
                height="16"
                viewBox="0 0 16 16"
                fill="none"
              >
                <path
                  d="M11.354 8.35375L6.35403 13.3538C6.30757 13.4002 6.25242 13.4371 6.19173 13.4622C6.13103 13.4873 6.06598 13.5003 6.00028 13.5003C5.93458 13.5003 5.86953 13.4873 5.80883 13.4622C5.74813 13.4371 5.69298 13.4002 5.64653 13.3538C5.60007 13.3073 5.56322 13.2521 5.53808 13.1915C5.51294 13.1308 5.5 13.0657 5.5 13C5.5 12.9343 5.51294 12.8693 5.53808 12.8086C5.56322 12.7479 5.60007 12.6927 5.64653 12.6463L10.2934 8L5.64653 3.35375C5.55271 3.25993 5.5 3.13269 5.5 3C5.5 2.86732 5.55271 2.74007 5.64653 2.64625C5.74035 2.55243 5.8676 2.49973 6.00028 2.49973C6.13296 2.49973 6.26021 2.55243 6.35403 2.64625L11.354 7.64625C11.4005 7.69269 11.4374 7.74783 11.4626 7.80853C11.4877 7.86923 11.5007 7.9343 11.5007 8C11.5007 8.06571 11.4877 8.13077 11.4626 8.19147C11.4374 8.25217 11.4005 8.30732 11.354 8.35375Z"
                  fill="black"
                />
              </svg>
            </Link>
          </li>
          <li>
            <Link to="">
              <p>FAQ</p>
              <svg
                xmlns="http://www.w3.org/2000/svg"
                width="16"
                height="16"
                viewBox="0 0 16 16"
                fill="none"
              >
                <path
                  d="M11.354 8.35375L6.35403 13.3538C6.30757 13.4002 6.25242 13.4371 6.19173 13.4622C6.13103 13.4873 6.06598 13.5003 6.00028 13.5003C5.93458 13.5003 5.86953 13.4873 5.80883 13.4622C5.74813 13.4371 5.69298 13.4002 5.64653 13.3538C5.60007 13.3073 5.56322 13.2521 5.53808 13.1915C5.51294 13.1308 5.5 13.0657 5.5 13C5.5 12.9343 5.51294 12.8693 5.53808 12.8086C5.56322 12.7479 5.60007 12.6927 5.64653 12.6463L10.2934 8L5.64653 3.35375C5.55271 3.25993 5.5 3.13269 5.5 3C5.5 2.86732 5.55271 2.74007 5.64653 2.64625C5.74035 2.55243 5.8676 2.49973 6.00028 2.49973C6.13296 2.49973 6.26021 2.55243 6.35403 2.64625L11.354 7.64625C11.4005 7.69269 11.4374 7.74783 11.4626 7.80853C11.4877 7.86923 11.5007 7.9343 11.5007 8C11.5007 8.06571 11.4877 8.13077 11.4626 8.19147C11.4374 8.25217 11.4005 8.30732 11.354 8.35375Z"
                  fill="black"
                />
              </svg>
            </Link>
          </li>
        </ul>
      </div>
      <div className="etc-section">
        <span>개인정보처리방침</span>
        <span> | </span>
        <span>서비스이용약관</span>
        <span> | </span>
        <span>v.1.0.0</span>
      </div>
    </div>
  );
};

export default Mypage;
