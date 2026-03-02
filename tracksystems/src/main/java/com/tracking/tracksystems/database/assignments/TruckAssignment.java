package com.tracking.tracksystems.database.assignments;

import com.tracking.tracksystems.database.users.User;
import com.tracking.tracksystems.database.trucks.Trucks;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "truck_assignments")
@Data
public class TruckAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assignment_id")
    private Long assignmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "truck_id", referencedColumnName = "id_truck", nullable = false)
    private Trucks truck;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id", referencedColumnName = "employee_id", nullable = false)
    private User driver;

    @CreationTimestamp
    @Column(name = "start_date", updatable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;
}