package br.com.sw2you.realmeet.domain.entity;

import static java.util.Objects.*;

import java.util.Objects;
import javax.persistence.*;

@Entity
@Table(name = "room")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "seats", nullable = false)
    private Integer seats;

    @Column(name = "active", nullable = false)
    private Boolean active;

    public Room() {}

    private Room(Long id, String name, Integer seats, Boolean active) {
        this.id = id;
        this.name = name;
        this.seats = seats;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getSeats() {
        return seats;
    }

    public Boolean getActive() {
        return active;
    }

    @PrePersist
    public void prePersist() {
        if (isNull(active)) {
            active = true;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return (
            Objects.equals(id, room.id) &&
            Objects.equals(name, room.name) &&
            Objects.equals(seats, room.seats) &&
            Objects.equals(active, room.active)
        );
    }

    @Override
    public int hashCode() {
        return hash(id, name, seats, active);
    }

    @Override
    public String toString() {
        return "Room{" + "id=" + id + ", name='" + name + '\'' + ", seats=" + seats + ", active=" + active + '}';
    }

    public static RoomBuilder newRoomBuilder() {
        return new RoomBuilder();
    }

    public static final class RoomBuilder {
        private Long id;
        private String name;
        private Integer seats;
        private Boolean active;

        private RoomBuilder() {}

        public RoomBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public RoomBuilder name(String name) {
            this.name = name;
            return this;
        }

        public RoomBuilder seats(Integer seats) {
            this.seats = seats;
            return this;
        }

        public RoomBuilder active(Boolean active) {
            this.active = active;
            return this;
        }

        public Room build() {
            return new Room(id, name, seats, active);
        }
    }
}
