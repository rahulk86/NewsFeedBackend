package com.NewFeed.backend.repository.messaging;

import com.NewFeed.backend.modal.messaging.UserConversation;
import com.NewFeed.backend.modal.messaging.UserMessenger;
import com.NewFeed.backend.modal.user.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserMessengerRepository extends JpaRepository<UserMessenger,Long> {
    @Query("select " +
            " reciverMessenger , " +
            " profileImage " +
            "from UserMessenger messenger " +
                "inner join UserMessenger reciverMessenger on " +
                    " messenger.conversation = reciverMessenger.conversation and" +
                    " messenger.profile = ?1 and" +
                    " reciverMessenger.profile != ?1 and" +
                    " messenger.active = 1 and" +
                    " reciverMessenger.active = 1 " +
                "left join Image profileImage on profileImage.imageableId = reciverMessenger.profile.id and" +
                    " profileImage.imageableType = 'UserProfile' and" +
                    " profileImage.active = 1 " )
    List<Object[]> findAllByUser(UserProfile profile);
//    @Query("select " +
//            " messenger , " +
//            " profileImage " +
//            "from UserMessenger messenger " +
//            "left join Image profileImage on profileImage.imageableId = messenger.sender.id and" +
//            " profileImage.imageableType = 'UserProfile' and" +
//            " profileImage.active = 1 " +
//            "where messenger.receiver = ?1 " )
//    List<Object[]> findAllByReceiver(UserProfile sender);
//    @Query("SELECT u FROM UserMessenger u " +
//            "WHERE (u.sender = :sender AND u.receiver = :receiver) OR " +
//            "(u.sender = :receiver AND u.receiver = :sender)")
    Optional<UserMessenger> findByProfileAndConversation(
            @Param("profile") UserProfile profile,
            @Param("conversation") UserConversation conversation
    );

}
