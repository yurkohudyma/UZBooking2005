package ua.hudyma.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.hudyma.domain.Notification;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM Notification n WHERE n.id IN :ids")
    void deleteByIds(@Param("ids") List<Long> ids);

    //delete n1_0 from notifications n1_0 where n1_0.id in (?,?,?)
}
