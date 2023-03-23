package com.ssafy.marathon.service.user;

import com.ssafy.marathon.db.entity.communication.Alarm;
import com.ssafy.marathon.db.entity.communication.Communication;
import com.ssafy.marathon.db.entity.communication.Message;
import com.ssafy.marathon.db.entity.user.User;
import com.ssafy.marathon.db.repository.CommunicationRepository;
import com.ssafy.marathon.db.repository.HistoryRepository;
import com.ssafy.marathon.db.repository.TreatmentRepository;
import com.ssafy.marathon.db.repository.UserRepository;
import com.ssafy.marathon.dto.request.communication.MessageReqDto;
import com.ssafy.marathon.dto.response.communication.CommunicationResDto;
import com.ssafy.marathon.dto.response.communication.UserCommuCntResDto;
import com.ssafy.marathon.dto.response.user.UserResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserCommunicationService {

    private final UserRepository<User> userRepository;
    private final CommunicationRepository<Communication> communicationRepository;
    private final TreatmentRepository treatmentRepository;
    private final HistoryRepository historyRepository;

    private final String admin = "[ROLE_ADMIN]";
    private final String doctor = "[ROLE_DOCTOR]";
    private final String patient = "[ROLE_PATIENT]";

    @Transactional
    public void sendMessage(Long senderSeq, MessageReqDto messageReqDto) {
        Optional<User> findSender = userRepository.findById(senderSeq);
        User sender = findSender.orElseThrow();

        Optional<User> findReceiver = userRepository.findById(messageReqDto.getReceiverSeq());
        User receiver = findReceiver.orElseThrow();

        Message message = Message.builder()
            .sender(sender)
            .receiver(receiver)
            .dateTime(LocalDateTime.now())
            .checked(false)
            .content(messageReqDto.getContent())
            .build();
        communicationRepository.save(message);
    }

    @Transactional(readOnly = true)
    public Page<CommunicationResDto> getCommunicationPages(Long userSeq, int pageNum) {
        Optional<User> findUser = userRepository.findById(userSeq);
        User user = findUser.orElseThrow();

        PageRequest pageRequest = PageRequest.of(pageNum - 1, 5);

        return communicationRepository.findAllByReceiverOrderByCheckedAscDateTimeDesc(user, pageRequest)
            .map(communication -> CommunicationResDto.builder()
                .commuSeq(communication.getSeq())
                .senderSeq(communication.getSender().getSeq())
                .senderName(communication.getSender().getName())
                .date(communication.getDateTime())
                .checked(communication.getChecked())
                .link(communication.getClass() == Alarm.class ? ((Alarm) communication).getLink() : null)
                .content(communication.getClass() == Message.class ? ((Message) communication).getContent() : null)
                .build());
    }

    @Transactional(readOnly = true)
    public void UpdateCheck(Long commuSeq) {
        Optional<Communication> findCommunication = communicationRepository.findById(commuSeq);
        Communication communication = findCommunication.orElseThrow();

        communication.changeChecked();
    }

    @Transactional(readOnly = true)
    public List<UserResDto> findCanSendMessageUsers(Long userSeq, String userRole, boolean isNew, Long commuSeq) {
        List<UserResDto> userResDtoList = new ArrayList<>();

        if (!isNew) {
            Optional<Communication> findMessage = communicationRepository.findById(commuSeq);
            Message beforeMessage = (Message) findMessage.orElseThrow();

            Optional<User> findSender = userRepository.findById(beforeMessage.getSender().getSeq());
            User sender = findSender.orElseThrow();

            userResDtoList.add(UserResDto.builder()
                .seq(sender.getSeq())
                .img(sender.getImg())
                .name(sender.getName())
                .id(sender.getId())
                .build());
        } else if (userRole.equals(admin)) {
            userResDtoList = userRepository.findAllByDtypeIsNot("Admin")
                .stream()
                .map(user -> UserResDto.builder()
                    .seq(user.getSeq())
                    .img(user.getImg())
                    .name(user.getName())
                    .id(user.getId())
                    .build()).collect(Collectors.toList());
        } else if (userRole.equals(doctor)) {
            userResDtoList = treatmentRepository.findDistinctByDoctor_Seq(userSeq)
                .stream()
                .map(treatment -> UserResDto.builder()
                    .seq(treatment.getPatient().getSeq())
                    .img(treatment.getPatient().getImg())
                    .name(treatment.getPatient().getName())
                    .id(treatment.getPatient().getId())
                    .build())
                .collect(Collectors.toList());

            userResDtoList.addAll(historyRepository.findDistinctByDoctor_Seq(userSeq)
                .stream()
                .map(history -> UserResDto.builder()
                    .seq(history.getPatient().getSeq())
                    .img(history.getPatient().getImg())
                    .name(history.getPatient().getName())
                    .id(history.getPatient().getId())
                    .build())
                .collect(Collectors.toList()));
        } else {
            userResDtoList = treatmentRepository.findDistinctByPatient_Seq(userSeq)
                .stream()
                .map(treatment -> UserResDto.builder()
                    .seq(treatment.getDoctor().getSeq())
                    .img(treatment.getDoctor().getImg())
                    .name(treatment.getDoctor().getName())
                    .id(treatment.getDoctor().getId())
                    .build())
                .collect(Collectors.toList());

            userResDtoList.addAll(historyRepository.findDistinctByPatient_Seq(userSeq)
                .stream()
                .map(history -> UserResDto.builder()
                    .seq(history.getDoctor().getSeq())
                    .img(history.getDoctor().getImg())
                    .name(history.getDoctor().getName())
                    .id(history.getDoctor().getId())
                    .build())
                .collect(Collectors.toList()));
        }

        return userResDtoList.stream()
            .distinct()
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserCommuCntResDto countUncheckedCommunication(Long userSeq) {
        Optional<User> findUser = userRepository.findById(userSeq);
        User user = findUser.orElseThrow();

        int count = communicationRepository.countByReceiverAndCheckedIsFalse(user);

        return UserCommuCntResDto.builder()
            .count(count)
            .build();
    }

    @Transactional
    public void deleteCommunication(Long commuSeq) {
        Optional<Communication> findCommunication = communicationRepository.findById(commuSeq);
        Communication communication = findCommunication.orElseThrow();

        communicationRepository.delete(communication);
    }
}