import { Component, signal , OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApiComponent } from '../../api.component';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { SensorRequest } from '../../app.dto';

@Component({
  selector: 'app-sensor-management',
  standalone: true,
  imports: [CommonModule , ReactiveFormsModule],
  templateUrl: './sensor-management.html',
})

export class SensorManagementComponent implements OnInit {

  sensorForm = new FormGroup({
    name : new FormControl(''),
    active : new FormControl(false)
  })


  addSensor = signal(false);

  constructor(private api : ApiComponent){}
  ngOnInit(): void {
    console.log("OK");
  } 

  SensorAdd(){
    this.addSensor.set(true);
  }

  CancelAdd(){
    this.addSensor.set(false);
  }

  OnRequest(){
    console.log(this.sensorForm.value as SensorRequest);
    this.api.PostSensor(this.sensorForm.value as SensorRequest).subscribe();
  }
}