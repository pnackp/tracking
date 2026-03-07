import { Component, OnInit, signal } from '@angular/core';
import { ApiComponent } from '../../../api.component';
import { AssignmentDTO, LocationDTO } from '../../../app.dto';

@Component({
  selector: 'app-assignment',
  templateUrl: './assignment.html',
  styleUrls: ['./assignment.css'],
})

export class Assignment implements OnInit {

  assignments = signal<AssignmentDTO[]>([]);
  locations = signal<LocationDTO[]>([]);
  loading = signal(false);
  errorMessage = signal('');

  constructor(private api: ApiComponent) {}

  ngOnInit(): void {
    this.LoadAssignments();
    this.LoadLocations();
  }

  LoadAssignments() {
    this.loading.set(true);

    this.api.GetFreeAssignments().subscribe({
      next: (data: AssignmentDTO[]) => {
        console.log(data);
        this.assignments.set(data);
        this.loading.set(false);
      },

      error: (err) => {
        console.error(err);
        this.errorMessage.set("Failed to load assignments");
        this.loading.set(false);
      }
    });
  }

  LoadLocations() {
    this.api.GetLocations().subscribe({
      next: (data: LocationDTO[]) => {
        console.log(data);
        this.locations.set(data);
      },

      error: (err) => {
        console.error(err);
        this.errorMessage.set("Failed to load locations");
      }
    });
  }

  getLocationName(id: number): string {
    const loc = this.locations().find(l => l.id === id);
    return loc ? `${loc.name}, ${loc.province}` : 'Unknown';
  }

}