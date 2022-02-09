package br.com.sw2you.realmeet.domain.entity;

import br.com.sw2you.realmeet.domain.model.Employee;
import br.com.sw2you.realmeet.util.DateUtils;
import java.time.OffsetDateTime;
import java.util.Objects;
import javax.persistence.*;

@Entity
@Table(name = "allocation")
public class Allocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @Embedded
    private Employee employee;

    @Column(name = "subject")
    private String subject;

    @Column(name = "start_at")
    private OffsetDateTime startAt;

    @Column(name = "end_at")
    private OffsetDateTime endAt;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    public Allocation() {}

    private Allocation(AllocationBuilder builder) {
        id = builder.id;
        room = builder.room;
        employee = builder.employee;
        subject = builder.subject;
        startAt = builder.startAt;
        endAt = builder.endAt;
        createdAt = builder.createdAt;
        updatedAt = builder.updatedAt;
    }

    @PrePersist
    public void prePersist() {
        if (Objects.isNull(createdAt)) {
            createdAt = DateUtils.now();
        }
        updatedAt = createdAt;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = DateUtils.now();
    }

    public Long getId() {
        return id;
    }

    public Room getRoom() {
        return room;
    }

    public Employee getEmployee() {
        return employee;
    }

    public String getSubject() {
        return subject;
    }

    public OffsetDateTime getStartAt() {
        return startAt;
    }

    public OffsetDateTime getEndAt() {
        return endAt;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Allocation that = (Allocation) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(room, that.room) &&
            Objects.equals(employee, that.employee) &&
            Objects.equals(subject, that.subject) &&
            Objects.equals(startAt, that.startAt) &&
            Objects.equals(endAt, that.endAt) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, room, employee, subject, startAt, endAt, createdAt, updatedAt);
    }

    @Override
    public String toString() {
        return (
            "Allocation{" +
            "id=" +
            id +
            ", room=" +
            room +
            ", employee=" +
            employee +
            ", subject='" +
            subject +
            '\'' +
            ", startAt=" +
            startAt +
            ", endAt=" +
            endAt +
            ", createdAt=" +
            createdAt +
            ", updatedAt=" +
            updatedAt +
            '}'
        );
    }

    public static AllocationBuilder newAllocationBuilder() {
        return new AllocationBuilder();
    }

    public static final class AllocationBuilder {
        private Long id;
        private Room room;
        private Employee employee;
        private String subject;
        private OffsetDateTime startAt;
        private OffsetDateTime endAt;
        private OffsetDateTime createdAt;
        private OffsetDateTime updatedAt;

        private AllocationBuilder() {}

        public AllocationBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public AllocationBuilder room(Room room) {
            this.room = room;
            return this;
        }

        public AllocationBuilder employee(Employee employee) {
            this.employee = employee;
            return this;
        }

        public AllocationBuilder subject(String subject) {
            this.subject = subject;
            return this;
        }

        public AllocationBuilder startAt(OffsetDateTime startAt) {
            this.startAt = startAt;
            return this;
        }

        public AllocationBuilder endAt(OffsetDateTime endAt) {
            this.endAt = endAt;
            return this;
        }

        public AllocationBuilder createdAt(OffsetDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public AllocationBuilder updatedAt(OffsetDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Allocation build() {
            return new Allocation(this);
        }
    }
}
