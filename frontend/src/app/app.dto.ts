export interface LoginPayload {
  identifier: string;
  password: string;
}

export interface RegisterRequest {
  employee_id: string;
  first_name: string;
  last_name: string;
  phone_number: string;
  email: string;
  password: string;
}

export interface SensorRequest{
  name : string,
  active : boolean
}

export interface Sensor{
  idSensor: number;
  sensorName: string;
  active: boolean;
  createdAt: Date;
}

export interface SensorUpdate{
  Id : number,
  active : boolean 
}

export interface Employee {
  employeeId: string;
  firstName: string;
  lastName: string;
  phone: string;
  email: string;
  createdAt: string;  
  emailVerify: boolean;
  isActive: boolean;
  role: string;
}

export interface EmployeeUpdateAdmin{
  is_active : boolean,
  role : string
}
