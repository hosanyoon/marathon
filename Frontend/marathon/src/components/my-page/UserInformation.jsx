import SelectBox from "components/common/SelectBox";
import { useEffect, useRef, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { changeNowSideNav } from "stores/toggle.store";
import style from "./UserInformation.module.css";
import { useQuery } from "@tanstack/react-query";
import { $ } from "util/axios";

/** 마이페이지 - 나의 정보 */
export default function UserInformation() {
  const state = useSelector((state) => state);
  const dispatch = useDispatch();

  const inputUserPwd = useRef();
  const inputUserPwdChk = useRef();
  const inputUserEmailId = useRef();
  const inputUserEmailHost = useRef();
  const inputUserPhone = useRef();
  const inputUserFirstResponder = useRef();
  const inputUserFirstResponderRelationship = useRef();
  const inputUserSecondResponder = useRef();
  const inputUserSecondResponderRelationship = useRef();
  const inputUserSelfIntroduce = useRef();

  const [userPwdMsg, setUserPwdMsg] = useState(
    "9자 이상, 16자 이하의 영문, 숫자, 특수문자를 조합해주세요."
  );
  const [userPwdChkMsg, setUserPwdChkMsg] = useState(
    "비밀번호를 한 번 더 기입해주세요."
  );

  const [isPwdValid, SetIsPwdValid] = useState(false);
  const [isPwdChkValid, SetIsPwdChkValid] = useState(false);
  const [isReadOnly, setIsReadOnly] = useState(false);

  const [userId, setUserId] = useState("");
  const [userName, setUserName] = useState("");
  const [userProfileImg, setUserProfileImg] = useState("");
  const [userSignUpDate, setUserSignUpDate] = useState("");
  const [userPwd, setUserPwd] = useState("");
  const [userPwdChk, setUserPwdChk] = useState("");
  const [userEmailId, setUserEmailId] = useState("");
  const [userEmailHost, setUserEmailHost] = useState("");
  const [userPhone, setUserPhone] = useState("");
  const [userFirstResponder, setUserFirstResponder] = useState("");
  const [userFirstResponderRelationship, setUserFirstResponderRelationship] =
    useState("none");
  const [userSecondResponder, setUserSecondResponder] = useState("");
  const [userSecondResponderRelationship, setUserSecondResponderRelationship] =
    useState("none");
  const [userSelfIntroduce, setUserSelfIntroduce] = useState("");

  /** 이메일 호스트 select box 옵션 */
  const optionEmailHost = [
    { value: "none", name: "직접 입력" },
    { value: "daum.net", name: "daum.net" },
    { value: "gmail.com", name: "gmail.com" },
    { value: "hanmail.net", name: "hanmail.net" },
    { value: "hotmail.com", name: "hotmail.com" },
    { value: "kakao.com", name: "kakao.com" },
    { value: "nate.com", name: "nate.com" },
    { value: "naver.com", name: "naver.com" },
  ];

  /** 비상 연락처 관계 select box 옵션 */
  const optionsRelationship = [
    {
      value: "none",
      name: "관계",
    },
    {
      value: "본인",
      name: "본인",
    },
    {
      value: "배우자",
      name: "배우자",
    },
    {
      value: "자녀",
      name: "자녀",
    },
    {
      value: "형제",
      name: "형제",
    },
    {
      value: "부모",
      name: "부모",
    },
    {
      value: "기타",
      name: "기타",
    },
  ];

  /** 이메일 호스트 select box를 선택했을 때 실행되는 함수 */
  const selectEmailHost = (x) => {
    if (x !== "none") {
      setIsReadOnly(true);
    } else {
      setIsReadOnly(false);
    }
    setUserEmailHost(x);
  };

  // const onSuccess = (data) => {
  //   console.log("------");
  //   console.log(data);
  // };

  // const onError = (data) => {
  //   console.log("에러났다");
  //   console.log(data);
  // };

  const { isLoading, data, isError, error } = useQuery(
    ["getUserInformation"],
    () => {
      $.get(`/patient-sign/modify`);
    },
    {
      onSuccess: (data) => {
        console.log("------");
        console.log(data);
      },
      onError: () => {
        console.log("에러났다");
        console.log(error);
      },
    }
  );
  if (isError) console.log(error);

  /** 맨 처음에는 유저 정보를 비동기 통신으로 받아온다. */
  useEffect(() => {
    // $.get(`/patient-sign/modify`)
    //   .then(({ data }) => {
    //     console.log(data);
    //     setUserProfileImg(state.loginUser.userProfileImg);
    //     setUserName(state.name);
    //     setUserEmailId(data.email.split("@")[0]);
    //     setUserId(state.id);
    //     setUserEmailHost(data.email.split("@")[1]);
    //     setUserPhone(data.phone);
    //     setUserSignUpDate(data.registDate);
    //     setUserFirstResponder(data.mainPhone);
    //     setUserFirstResponderRelationship(data.mainRelationship);
    //     setUserSecondResponder(data.subPhone);
    //     setUserSecondResponderRelationship(data.subRelationship);
    //     // setUserSelfIntroduce(data.);
    //   })
    //   .catch((error) => {
    //     console.log(error);
    //   });

    // 사이드 Nav 초기화
    dispatch(changeNowSideNav("회원 정보 관리"));
  }, []);

  /** 비밀번호 유효성 체크 */
  useEffect(() => {
    if (userPwd.length === 0) {
      setUserPwdMsg(
        "9자 이상, 16자 이하의 영문, 숫자, 특수문자를 조합해주세요."
      );
      SetIsPwdValid(false);
    } else if (userPwd.length < 9) {
      setUserPwdMsg("최소 9글자를 입력해야 합니다.");
      SetIsPwdValid(false);
    } else if (userPwd.length > 16) {
      setUserPwdMsg("최대 16자까지 입력 가능합니다.");
      SetIsPwdValid(false);
    } else {
      let isAlpha = false;
      let isNumber = false;
      let isSpecial = false;
      [...userPwd].forEach((element) => {
        if (
          ("a" <= element && element <= "z") ||
          ("A" <= element && element <= "Z")
        )
          isAlpha = true;
        else if ("1" <= element && element <= "9") isNumber = true;
        else isSpecial = true;
      });
      if (isAlpha && isNumber && isSpecial) {
        setUserPwdMsg("사용 가능한 비밀번호입니다.");
        SetIsPwdValid(true);
      } else {
        setUserPwdMsg("비밀번호는 영문, 숫자, 특수문자가 조합되어야 합니다.");
        SetIsPwdValid(false);
      }
    }
  }, [userPwd]);

  /** 비밀번호가 일치하는지 확인 */
  useEffect(() => {
    if (userPwdChk === "" || userPwdChk === null) {
      setUserPwdChkMsg("비밀번호를 한 번 더 기입해주세요.");
      SetIsPwdChkValid(false);
    } else if (userPwd === userPwdChk) {
      setUserPwdChkMsg("입력한 비밀번호와 일치합니다.");
      SetIsPwdChkValid(true);
    } else {
      setUserPwdChkMsg("입력한 비밀번호와 일치하지 않습니다.");
      SetIsPwdChkValid(false);
    }
  }, [userPwd, userPwdChk]);

  /** 연락처가 유효한지 체크하는 함수 */
  const chkPhone = (phone) => {
    if (phone.length < 9) return false;
    for (let i = 0; i < phone.length; i++) {
      if ("0" > phone[i] || "9" < phone[i]) return false;
    }
    return true;
  };

  /** 회원탈퇴 버튼을 누르면 실행되는 함수 */
  const unregister = () => {};

  /** 수정완료 버튼을 누르면 실행되는 함수 */
  const modify = () => {
    if (!isPwdValid) {
      alert("비밀번호가 유효하지 않습니다.");
      inputUserPwd.current.focus();
    } else if (!isPwdChkValid) {
      alert("비밀번호 확인이 일치하지 않습니다.");
      inputUserPwdChk.current.focus();
    } else if (userEmailId === "" || userEmailId === null) {
      alert("이메일을 입력해주세요.");
      inputUserEmailId.current.focus();
    } else if (
      userEmailHost === "" ||
      userEmailHost === null ||
      userEmailHost === "none"
    ) {
      alert("이메일을 입력해주세요.");
      inputUserEmailHost.current.focus();
    } else if (userPhone === "" || userPhone === null) {
      alert("연락처를 입력해주세요.");
      inputUserPhone.current.focus();
    } else if (!chkPhone(userPhone)) {
      alert("연락처가 유효하지 않습니다.");
      inputUserPhone.current.focus();
    } else if (
      state.loginUser.userRole === "normal" &&
      (userFirstResponder === "" || userFirstResponder === null)
    ) {
      alert("비상 연락처 1을 입력해주세요.");
      inputUserFirstResponder.current.focus();
    } else if (
      state.loginUser.userRole === "normal" &&
      !chkPhone(userFirstResponder)
    ) {
      alert("연락처가 유효하지 않습니다.");
      inputUserFirstResponder.current.focus();
    } else if (
      state.loginUser.userRole === "normal" &&
      (userFirstResponderRelationship === "none" ||
        userFirstResponderRelationship === null)
    ) {
      alert("비상 연락처 1의 관계를 입력해주세요.");
      inputUserFirstResponderRelationship.current.focus();
    } else if (
      state.loginUser.userRole === "normal" &&
      userSecondResponderRelationship !== "none" &&
      userSecondResponderRelationship !== null &&
      (userSecondResponder === "" || userSecondResponder === null)
    ) {
      alert("비상 연락처 2를 입력해주세요.");
      inputUserSecondResponder.current.focus();
    } else if (
      state.loginUser.userRole === "normal" &&
      userSecondResponder.length > 0 &&
      !chkPhone(userSecondResponder)
    ) {
      alert("연락처가 유효하지 않습니다.");
      inputUserSecondResponder.current.focus();
    } else if (
      state.loginUser.userRole === "normal" &&
      userSecondResponder.length > 0 &&
      (userSecondResponderRelationship === "none" ||
        userSecondResponderRelationship === null)
    ) {
      alert("비상 연락처 2의 관계를 입력해주세요.");
      inputUserSecondResponderRelationship.current.focus();
    } else {
      alert("수정 완료되었습니다.");
      setUserPwd("");
      setUserPwdChk("");
      console.log({
        userPwd: userPwd,
        userEmailId: userEmailId,
        userEmailHost: userEmailHost,
        userPhone: userPhone,
        userFirstResponder: userFirstResponder,
        userFirstResponderRelationship: userFirstResponderRelationship,
        userSecondResponder: userSecondResponder,
        userSecondResponderRelationship: userFirstResponderRelationship,
        userSelfIntroduce: userSelfIntroduce,
      });
    }
  };

  return (
    <div className={style.side_right_board}>
      <h2>나의 정보</h2>
      <div className={style.information_box}>
        {/* 왼쪽 박스 */}
        <div className={style.left_box}>
          {/* 프로필 사진 */}
          {userProfileImg === null ||
          userProfileImg === "" ||
          userProfileImg === undefined ? (
            <div className={style.profile_img + " " + style.profile_initial}>
              A
            </div>
          ) : (
            <img
              className={style.profile_img}
              src={userProfileImg}
              alt="프로필 사진"
            />
          )}
          <div className={style.user_name}>{userName} 님</div>
          <div className={style.welcome}>환영합니다.</div>
          <label htmlFor="file" className={style.btn_upload}>
            사진 업로드
          </label>
          <input className={style.btn_upload} type="file" id="file" />

          <hr className={style.left_center_line} />
          <div className={style.sub_title}>아이디</div>
          <div className={style.sub_content}>{userId}</div>
          <div className={style.sub_title}>가입 날짜</div>
          <div className={style.sub_content}>{userSignUpDate}</div>
        </div>
        <hr className={style.center_line} />
        {/* 오른쪽 박스 */}
        <div className={style.right_box}>
          {/* input */}
          <div>
            {/* 비밀번호 */}
            <div className={style.input_div}>
              <label className={style.input_label} htmlFor="user_pwd">
                비밀번호
              </label>
              <input
                className={style.input_long}
                type="password"
                id="user_pwd"
                maxLength="16"
                value={userPwd}
                onChange={(e) => {
                  setUserPwd(e.target.value);
                }}
                ref={inputUserPwd}
              />
              <div
                className={`${style.sub_information}`}
                style={
                  userPwd === ""
                    ? { color: "#858585" }
                    : isPwdValid
                    ? { color: "blue" }
                    : { color: "red" }
                }
              >
                {userPwdMsg}
              </div>
            </div>
            {/* 비밀번호 확인 */}
            <div className={style.input_div}>
              <label className={style.input_label} htmlFor="user_pwd_chk">
                비밀번호 확인
              </label>
              <input
                className={style.input_long}
                type="password"
                id="user_pwd_chk"
                maxLength="16"
                value={userPwdChk}
                onChange={(e) => {
                  setUserPwdChk(e.target.value);
                }}
                ref={inputUserPwdChk}
              />
              <div
                className={`${style.sub_information}`}
                style={
                  userPwdChk === ""
                    ? { color: "#858585" }
                    : isPwdChkValid
                    ? { color: "blue" }
                    : { color: "red" }
                }
              >
                {userPwdChkMsg}
              </div>
            </div>
            {/* 이메일 */}
            <div className={style.input_div}>
              <label className={style.input_label} htmlFor="user_email_id">
                이메일
              </label>
              <input
                className={`${style.input_email_id}`}
                type="text"
                id="user_email_id"
                value={userEmailId}
                onChange={(e) => {
                  setUserEmailId(e.target.value);
                }}
                ref={inputUserEmailId}
              />
              <span className={style.at}>@</span>

              <input
                className={style.input_email_host}
                type="text"
                id="input_email_host"
                readOnly={isReadOnly}
                value={userEmailHost === "none" ? "" : userEmailHost}
                onChange={(e) => setUserEmailHost(e.target.value)}
                ref={inputUserEmailHost}
              />
              <SelectBox
                options={optionEmailHost}
                onChange={(x) => selectEmailHost(x)}
              />
            </div>
            {/* (주) 연락처 */}
            <div className={style.input_div}>
              <label className={style.input_label} htmlFor="user_phone">
                {state.loginUser.userRole === "normal" ? (
                  <>주 연락처</>
                ) : (
                  <>연락처</>
                )}
              </label>
              <input
                className={`${style.input_number} ${style.input_long}`}
                type="number"
                id="user_phone"
                placeholder="'-'를 제외한 숫자만 입력해 주세요."
                value={userPhone}
                onChange={(e) => {
                  setUserPhone(e.target.value);
                }}
                ref={inputUserPhone}
              />
            </div>
            {/* userRole에 따라서 달라지는 내용 */}
            {state.loginUser.userRole === "normal" ? (
              <>
                <div className={style.input_div}>
                  <label
                    className={style.input_label}
                    htmlFor="user_first_responder"
                  >
                    비상 연락처 1
                  </label>
                  <input
                    className={`${style.input_number} ${style.input_middle}`}
                    type="number"
                    id="user_first_responder"
                    placeholder="'-'를 제외한 숫자만 입력해 주세요."
                    value={userFirstResponder}
                    onChange={(e) => {
                      setUserFirstResponder(e.target.value);
                    }}
                    ref={inputUserFirstResponder}
                  />
                  <SelectBox
                    options={optionsRelationship}
                    onChange={(x) => {
                      setUserFirstResponderRelationship(x);
                    }}
                    defaultValue={userFirstResponderRelationship}
                    ref={inputUserFirstResponderRelationship}
                  />
                </div>
                <div className={style.input_div}>
                  <label
                    className={style.input_label}
                    htmlFor="user_second_responder"
                  >
                    비상 연락처 2 (선택)
                  </label>
                  <input
                    className={`${style.input_number} ${style.input_middle}`}
                    type="number"
                    id="user_second_responder"
                    placeholder="'-'를 제외한 숫자만 입력해 주세요."
                    value={userSecondResponder}
                    onChange={(e) => {
                      setUserSecondResponder(e.target.value);
                    }}
                    ref={inputUserSecondResponder}
                  />
                  <SelectBox
                    options={optionsRelationship}
                    onChange={(x) => setUserSecondResponderRelationship(x)}
                    defaultValue={userSecondResponderRelationship}
                    ref={inputUserSecondResponderRelationship}
                  />
                </div>
              </>
            ) : state.loginUser.userRole === "doctor" ? (
              <>
                <div className={style.input_div}>
                  <label
                    className={style.input_label}
                    htmlFor="user_self_introduce"
                  >
                    자기소개
                  </label>
                  <textarea
                    className={style.input_textarea}
                    onChange={(e) => {
                      setUserSelfIntroduce(e.target.value);
                    }}
                    value={userSelfIntroduce}
                    id="user_self_introduce"
                    ref={inputUserSelfIntroduce}
                    maxLength="174"
                    placeholder="이용자들에게 보여질 자기소개 글을 작성해주세요."
                  />
                </div>
              </>
            ) : null}
          </div>
          <div className={style.btns}>
            <button className={style.btn_unregister} onClick={unregister}>
              회원탈퇴
            </button>
            <button className={style.btn_modify} onClick={modify}>
              수정완료
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
