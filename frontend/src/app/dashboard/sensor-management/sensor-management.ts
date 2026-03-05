import { Component, signal, OnInit, Signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApiComponent } from '../../api.component';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { SensorRequest, Sensor, SensorUpdate } from '../../app.dto';

@Component({
  selector: 'app-sensor-management',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './sensor-management.html',
})

export class SensorManagementComponent implements OnInit {

  sensorForm = new FormGroup({
    name: new FormControl(''),
    active: new FormControl(false)
  })

  addSensor = signal(false);

  constructor(private api: ApiComponent) { }

  sensors = signal<Sensor[]>([]);

  isLoading = signal(true);

  confirmDelete = signal(false)

  ngOnInit() {
    this.loadSensors();
    console.log("hello");
  }

  loadSensors() {
    this.isLoading.set(true);

    this.api.GetSensor().subscribe({
      next: res => {
        this.sensors.set(res);
        this.isLoading.set(false);
      },
      error: err => {
        console.error(err);
        this.isLoading.set(false);
      }
    });
  }

  SensorAdd() {
    this.addSensor.set(true);
  }

  CancelAdd() {
    this.addSensor.set(false);
  }

  OnRequest() {

    if (this.sensorForm.invalid) return;

    const payload = this.sensorForm.value as SensorRequest;

    this.api.PostSensor(payload).subscribe({
      next: () => {
        this.loadSensors();
        this.addSensor.set(false);
        this.sensorForm.reset({
          name: '',
          active: false
        });

      },
      error: err => {
        console.error(err);
      }
    });
  }

  ChangeStatus(sensor: Sensor) {
    const payload: SensorUpdate = {
      Id: sensor.idSensor,
      active: !sensor.active
    };

    this.api.PutSensor(payload).subscribe({
      next: () => {
        this.sensors.update(list =>
          list.map(s =>
            s.idSensor === sensor.idSensor
              ? { ...s, active: payload.active }
              : s
          )
        );

      },
      error: err => {
        console.error(err);
      }
    });
  }

  selectedSensor = signal<Sensor | null>(null);

  ConfirmDelete(sensor: Sensor) {
    this.confirmDelete.set(true);
    this.selectedSensor.set(sensor);
  }

  CancelConfirm() {
    this.confirmDelete.set(false);
  }

  RequestDelete() {
    const sensor = this.selectedSensor();
    if (!sensor) return;

    const id = Number(sensor.idSensor);

    this.api.DeleteSensor(id).subscribe({
      next: () => {
        this.sensors.update(list =>
          list.filter(s => s.idSensor !== sensor.idSensor)
        );
        this.confirmDelete.set(false);
      },
      error: err => {
        console.error(err);
      }
    });
  }
}