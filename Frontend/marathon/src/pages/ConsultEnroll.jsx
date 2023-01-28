import { React, useEffect, useRef, useState } from "react";
import style from "./ConsultEnroll.module.css";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faFileLines,
  faCalendarCheck,
} from "@fortawesome/free-regular-svg-icons";
import { faCamera } from "@fortawesome/free-solid-svg-icons";
import { useNavigate } from "react-router-dom";
import SelectBox from "components/common/SelectBox";
import Calendar from "react-calendar";
import "Calendar.css";

export default function ConsultEnroll() {
  /** 성별 select box 옵션 */
  const optionsGender = [
    { value: "none", name: "성별" },
    { value: "male", name: "남성" },
    { value: "female", name: "여성" },
  ];

  /** 태어난 년도 select box 옵션 */
  const today = new Date();
  const year = today.getFullYear();

  const optionsYear = [{ value: "none", name: "년도" }];

  for (let i = 0; i < 101; i++) {
    optionsYear.push({ value: `${year - i}`, name: `${year - i}` });
  }

  /** 태어난 월 select box 옵션 */
  const optionsMonth = [
    { value: "none", name: "월" },
    { value: "1", name: "1월" },
    { value: "2", name: "2월" },
    { value: "3", name: "3월" },
    { value: "4", name: "4월" },
    { value: "5", name: "5월" },
    { value: "6", name: "6월" },
    { value: "7", name: "7월" },
    { value: "8", name: "8월" },
    { value: "9", name: "9월" },
    { value: "10", name: "10월" },
    { value: "11", name: "11월" },
    { value: "12", name: "12월" },
  ];

  /** 이메일 호스트 select box 옵션 */
  const optionHost = [
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

  const [calenderDay, setCalenderDay] = useState(new Date());
  const navigate = useNavigate();

  const inputUserName = useRef();
  const inputUserGender = useRef();
  const inputUserYear = useRef();
  const inputUserMonth = useRef();
  const inputUserDay = useRef();
  const inputUserEmailId = useRef();
  const inputUserEmailHost = useRef();
  const inputUserPhone = useRef();
  const inputUserFirstResponder = useRef();
  const inputUserFirstResponderRelationship = useRef();
  const inputUserSecondResponder = useRef();
  const inputUserSecondResponderRelationship = useRef();
  const inputUserTos = useRef();
  const inputDiseaseTos = useRef();
  const inputDiseaseYear = useRef();
  const inputDiseaseMonth = useRef();
  const inputDiseaseDay = useRef();
  const inputDesTextarea = useRef();

  const [userName, setUserName] = useState("");
  const [userGender, setUserGender] = useState("none");
  const [userYear, setUserYear] = useState("none");
  const [userMonth, setUserMonth] = useState("none");
  const [userDay, setUserDay] = useState();
  const [userEmailId, setUserEmailId] = useState("");
  const [userEmailHost, setUserEmailHost] = useState("");
  const [userPhone, setUserPhone] = useState("");
  const [userFirstResponder, setUserFirstResponder] = useState("");
  const [userFirstResponderRelationship, setUserFirstResponderRelationship] =
    useState("none");
  const [userSecondResponder, setUserSecondResponder] = useState("");
  const [userSecondResponderRelationship, setUserSecondResponderRelationship] =
    useState("none");
  const [userTos, setUserTos] = useState(false);
  const [diseaseTos, setDiseaseTos] = useState(false);
  const [diseaseYear, setDiseaseYear] = useState("none");
  const [diseaseMonth, setDiseaseMonth] = useState("none");
  const [diseaseDay, setDiseaseDay] = useState("none");
  const [desTextarea, setDesTextarea] = useState("");

  const [isReadOnly, setIsReadOnly] = useState(false);
  const [optionsDay, setOptionsDay] = useState([{ value: "none", name: "일" }]);
  const [optionsDay2, setOptionsDay2] = useState([
    { value: "none", name: "일" },
  ]);

  /** 신청하기 버튼을 누를 때 실행되는 함수 */
  const checkUp = () => {
    if (userName === "" || userName === null) {
      alert("이름을 입력해주세요.");
      inputUserName.current.focus();
    } else if (userGender === "none" || userGender === null) {
      alert("성별을 선택해주세요.");
      inputUserGender.current.focus();
    } else if (userYear === "none" || userYear === null) {
      alert("생년월일을 선택해주세요.");
      inputUserYear.current.focus();
    } else if (userMonth === "none" || userMonth === null) {
      alert("생년월일을 선택해주세요.");
      inputUserMonth.current.focus();
    } else if (userDay === "none" || userDay === null) {
      alert("생년월일을 선택해주세요.");
      inputUserDay.current.focus();
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
    } else if (userFirstResponder === "" || userFirstResponder === null) {
      alert("비상 연락처 1을 입력해주세요.");
      inputUserFirstResponder.current.focus();
    } else if (!chkPhone(userFirstResponder)) {
      alert("연락처가 유효하지 않습니다.");
      inputUserFirstResponder.current.focus();
    } else if (
      userFirstResponderRelationship === "none" ||
      userFirstResponderRelationship === null
    ) {
      alert("비상 연락처 1의 관계를 입력해주세요.");
      inputUserFirstResponderRelationship.current.focus();
    } else if (
      userSecondResponderRelationship !== "none" &&
      userSecondResponderRelationship !== null &&
      (userSecondResponder === "" || userSecondResponder === null)
    ) {
      alert("비상 연락처 2를 입력해주세요.");
      inputUserSecondResponder.current.focus();
    } else if (
      userSecondResponder.length > 0 &&
      !chkPhone(userSecondResponder)
    ) {
      alert("연락처가 유효하지 않습니다.");
      inputUserSecondResponder.current.focus();
    } else if (
      userSecondResponder.length > 0 &&
      (userSecondResponderRelationship === "none" ||
        userSecondResponderRelationship === null)
    ) {
      alert("비상 연락처 2의 관계를 입력해주세요.");
      inputUserSecondResponderRelationship.current.focus();
    } else if (!diseaseTos) {
      if (diseaseYear === "none" || diseaseYear === null) {
        alert("발병일을 선택해주세요.");
        inputDiseaseYear.current.focus();
      } else if (diseaseMonth === "none" || diseaseMonth === null) {
        alert("발병일을 선택해주세요.");
        inputDiseaseMonth.current.focus();
      } else if (diseaseDay === "none" || diseaseDay === null) {
        alert("발병일을 선택해주세요.");
        inputDiseaseDay.current.focus();
      }
    } else if (desTextarea === "") {
      alert("소통에 어려움 점을 입력해 주세요.");
      inputDesTextarea.current.focue();
    } else if (!userTos) {
      alert("이용약관 및 개인정보 처리방침에 동의해주세요.");
      inputUserTos.current.focue();
    } else {
      navigate("/");
    }
  };

  /** 연락처가 유효한지 체크하는 함수 */
  const chkPhone = (phone) => {
    if (phone.length < 9) return false;
    for (let i = 0; i < phone.length; i++) {
      if ("0" > phone[i] || "9" < phone[i]) return false;
    }
    return true;
  };

  const selectEmailHost = (x) => {
    if (x !== "none") {
      setIsReadOnly(true);
    } else {
      setIsReadOnly(false);
    }
    setUserEmailHost(x);
  };

  /** year와 month가 선택되고 난 뒤 day 일 수를 결정 */
  useEffect(() => {
    if (userYear !== "none" && userMonth !== "none") {
      let days = [{ value: "none", name: "일" }];
      const day = new Date(userYear, userMonth, 0).getDate();

      for (let i = 1; i <= day; i++) {
        days.push({ value: `${i}`, name: `${i}일` });
      }

      setOptionsDay(days);
    }
  }, [userYear, userMonth]);

  /** year와 month가 선택되고 난 뒤 day 일 수를 결정 */
  useEffect(() => {
    if (diseaseYear !== "none" && diseaseMonth !== "none") {
      let days = [{ value: "none", name: "일" }];
      const day = new Date(diseaseYear, diseaseMonth, 0).getDate();

      for (let i = 1; i <= day; i++) {
        days.push({ value: `${i}`, name: `${i}일` });
      }

      setOptionsDay2(days);
    }
  }, [diseaseYear, diseaseMonth]);

  useEffect(() => {
    let today_day = new Date().getDate();
    let today_month = new Date().getMonth();
    let today_year = new Date().getFullYear();
    if (
      calenderDay.getFullYear() < today_year ||
      calenderDay.getMonth() < today_month ||
      (calenderDay.getMonth() === today_month &&
        calenderDay.getDate() < today_day)
    ) {
      alert("유효한 날짜가 아닙니다.");
      setCalenderDay(new Date());
    }
  }, [calenderDay]);

  return (
    <div className="container">
      <div className="inner_container" style={{ paddingBottom: "10px" }}>
        <div className={style.color_box}>
          <div className={style.title_box}>신청절차</div>
          <h4>
            먼저 상담을 통해서 환자의 상태와 원격 재활이 가능한 지 논의해
            보세요!
          </h4>
          <div className={style.icon_contain_box}>
            <div>
              <FontAwesomeIcon className={style.icon_box} icon={faFileLines} />
              <h5>상담 신청서 제출</h5>
            </div>
            <div>
              <FontAwesomeIcon className={style.icon_box} icon={faCamera} />
              <h5>부담없는 화상 상담</h5>
            </div>
            <div>
              <FontAwesomeIcon
                className={style.icon_box}
                icon={faCalendarCheck}
              />
              <h5>재활사 선택 / 예약 확정</h5>
            </div>
          </div>
        </div>
      </div>
      <div className="inner_container" style={{ width: "90%" }}>
        <div className={style.inner_container}>
          <div>
            <p style={{ marginBottom: "1px" }}>상담 희망날짜</p>
            {/* 달력 컴포넌트 */}
            <Calendar onChange={setCalenderDay} value={calenderDay} />
          </div>

          {/* 성명 & 성별 */}
          <div className={style.input_div}>
            <label className={style.input_label} htmlFor="user_name">
              대상자 성명
            </label>
            <input
              className={style.input_middle}
              onChange={(e) => {
                setUserName(e.target.value);
              }}
              value={userName}
              type="text"
              id="user_name"
              ref={inputUserName}
            />
            <SelectBox
              options={optionsGender}
              onChange={(x) => setUserGender(x)}
              ref={inputUserGender}
            />
          </div>

          {/* 생년월일 */}
          <div className={style.input_div}>
            <label className={style.input_label}>대상자 생년월일</label>
            <div
              style={{
                width: "412px",
                display: "flex",
                justifyContent: "space-between",
              }}
            >
              <SelectBox
                options={optionsYear}
                onChange={(x) => setUserYear(x)}
                width="191px"
                ref={inputUserYear}
              />
              <SelectBox
                options={optionsMonth}
                onChange={(x) => setUserMonth(x)}
                ref={inputUserMonth}
              />
              <SelectBox
                options={optionsDay}
                onChange={(x) => setUserDay(x)}
                ref={inputUserDay}
              />
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
              options={optionHost}
              onChange={(x) => selectEmailHost(x)}
            />
          </div>

          {/* (주) 연락처 */}
          <div className={style.input_div}>
            <label className={style.input_label} htmlFor="user_phone">
              <>주 연락처</>
            </label>
            <input
              className={`${style.input_number} ${style.input_long}`}
              type="text"
              id="user_phone"
              placeholder="'-'를 제외한 숫자만 입력해 주세요."
              value={userPhone}
              onChange={(e) => {
                setUserPhone(e.target.value);
              }}
              ref={inputUserPhone}
            />
          </div>

          {/* 비상 연락처 */}
          <div className={style.input_div}>
            <label className={style.input_label} htmlFor="user_first_responder">
              비상 연락처 1
            </label>
            <input
              className={`${style.input_number} ${style.input_middle}`}
              type="text"
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
              ref={inputUserFirstResponderRelationship}
            />
            <div className={`${style.sub_information}`}>
              참관 링크 메시지가 발송되는 연락처입니다.
            </div>
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
              type="text"
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
              ref={inputUserSecondResponderRelationship}
            />
          </div>

          {/* 발병일 */}
          <div className={style.input_div}>
            <label className={style.input_label}>
              대상자의 뇌손상 발병시기
              <div style={{ display: "inline-block", marginLeft: "140px" }}>
                <input
                  className={style.input_check_box2 + " " + style.inline}
                  type="checkbox"
                  id="tos2"
                  value={diseaseTos}
                  onChange={(e) => {
                    setDiseaseTos(e.target.checked);
                  }}
                  ref={inputDiseaseTos}
                />
                <label
                  className={`${style.input_label} ${style.inline}`}
                  htmlFor="tos2"
                >
                  해당사항 없음
                </label>
              </div>
            </label>
            <div
              style={{
                width: "412px",
                display: "flex",
                justifyContent: "space-between",
              }}
            >
              <SelectBox
                options={optionsYear}
                onChange={(x) => setDiseaseYear(x)}
                width="191px"
                ref={inputDiseaseYear}
              />
              <SelectBox
                options={optionsMonth}
                onChange={(x) => setDiseaseMonth(x)}
                ref={inputDiseaseMonth}
              />
              <SelectBox
                options={optionsDay2}
                onChange={(x) => setDiseaseDay(x)}
                ref={inputDiseaseDay}
              />
            </div>
          </div>

          {/* 재활에 어려운 점 */}
          <div style={{ marginTop: "30px" }}>
            <label className={style.input_label} htmlFor="des_textarea">
              의사소통에서 어려움을 느끼는 부분을 말해주세요!
            </label>
            <textarea
              className={style.input_textarea}
              onChange={(e) => {
                setDesTextarea(e.target.value);
              }}
              value={desTextarea}
              id="des_textarea"
              ref={inputDesTextarea}
              maxLength="174"
            />
          </div>
        </div>

        <div className={style.private_info_box}>
          <h5>개인정보 처리 관련 내용</h5>
          <p>1. 수집하는 개인정보 항목: 성명, 연령, 이메일, 전화번호</p>
          <br />
          <p>
            2. 개인정보의 수집 및 이용 목적: 제공하신 정보는 언어발전소의
            언어재활 상담과 예약을 위해 사용합니다.
          </p>
          <p>
            (1) 본인 확인 식별(동명이인 등) 절차에 이용 (성명, 연령, 이메일,
            전화번호)
          </p>
          <p>(2) 의사소통 및 정보 전달 등에 이용 (성명, 이메일, 전화번호)</p>
          <br />
          <p>3. 개인정보의 보유 및 이용</p>
          <p>
            기간: 수집된 개인정보의 보유 기간은 언어재활 상담 종료 후 1년간이며
            삭제 요청시 즉시 파기합니다.
          </p>
          <p>
            귀하는 이에 대한 동의를 거부할 수 있습니다만, 동의가 없을 시 무료
            언어재활 상담이 불가능할 수 있음을 알려드립니다.
          </p>
        </div>

        {/* 이용약관 동의 */}
        <div className={style.info_check_box}>
          <input
            className={style.input_check_box + " " + style.inline}
            type="checkbox"
            id="tos"
            value={userTos}
            onChange={(e) => {
              setUserTos(e.target.checked);
            }}
            ref={inputUserTos}
          />
          <label
            className={`${style.input_label} ${style.inline}`}
            style={{ marginLeft: "10px" }}
            htmlFor="tos"
          >
            <span style={{ color: "blue" }}>개인정보 수집 및 이용</span>에
            동의합니다.
          </label>
        </div>

        <button
          className={style.enroll_btn}
          onClick={() => {
            checkUp();
          }}
        >
          신청하기
        </button>
      </div>
    </div>
  );
}