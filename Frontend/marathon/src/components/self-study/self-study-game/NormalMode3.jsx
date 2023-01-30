import { useEffect } from "react";
import { useDispatch } from "react-redux";
import { useParams } from "react-router-dom";
import { addRecord, resetRecord } from "stores/game.store";
import commonStyle from "./Game.module.css";

export default function EasyMode1() {
  /** 10단계 중 몇 번째 단계 게임을 하고 있는지 */
  const stage = useParams().stage;

  const dispatch = useDispatch();

  useEffect(() => {
    /** 1단계라면 점수 기록을 초기화 */
    if (stage == 1) {
      dispatch(resetRecord());
    }

    /** 정답을 맞췄다면 아래 코드 실행 */
    dispatch(addRecord(true));

    /** 정답을 틀렸다면 아래 코드 실행 */
    // dispatch(addRecord(false));
  }, [stage]);

  return (
    <>
      <div className={commonStyle.stage}>{stage} / 10</div>
      <div className={commonStyle.title}>
        -------여기에 가이드 문구를 입력해주세요-------
      </div>
      <div>
        {/* --------------여기에 게임을 구현해주세요----------------- */}
      </div>
    </>
  );
}