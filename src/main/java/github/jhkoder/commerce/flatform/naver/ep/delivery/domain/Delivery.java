package github.jhkoder.commerce.flatform.naver.ep.delivery.domain;
import github.jhkoder.commerce.common.entity.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "delivery")
public class Delivery  extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "shipping", nullable = false,length = 100)
    private int shipping; // 배송료

    @Column(name = "delivery_grade", nullable = false,length = 1)
    private String deliveryGrade; // 차등배송비 여부

    @Column(name = "delivery_detail",length = 100)
    private String deliveryDetail; // 차등 배송비 내용
}
