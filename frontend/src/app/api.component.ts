import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { LoginPayload, RegisterRequest, SensorRequest , Sensor , SensorUpdate , Employee, EmployeeUpdateAdmin} from './app.dto';

@Injectable({ providedIn: 'root' })
export class ApiComponent {
    constructor(private http: HttpClient) { }

    CheckPremission(){
        return this.http.get("http://localhost:8080/api/check/checkcookie" , {withCredentials : true , responseType: 'text'})
    }

    LoginRequest(data: LoginPayload) {
        return this.http.post("http://localhost:8080/api/auth/login",data,{withCredentials: true});
    }

    RegisterRequest(data : RegisterRequest){
        return this.http.post("http://localhost:8080/api/auth/register",data,{withCredentials : true})
    }

    LogoutRequest(){
        return this.http.get("http://localhost:8080/api/auth/logout",{withCredentials : true , responseType: 'text'});
    }

    GetSensor(){
        return this.http.get<Sensor[]>("http://localhost:8080/api/admin/sensor" , {withCredentials : true });
    }

    PostSensor(payload : SensorRequest){
        return this.http.post("http://localhost:8080/api/admin/sensor", payload , {withCredentials : true , responseType: 'text' });
    }

    PutSensor(payload : SensorUpdate){
        return this.http.put("http://localhost:8080/api/admin/sensor" , payload , {withCredentials : true , responseType: 'text' }) 
    }

    DeleteSensor(payload : number){
        return this.http.delete(`http://localhost:8080/api/admin/sensor/${payload}`,{withCredentials : true , responseType: 'text' })
    }

    GetEmployees(){
        return this.http.get<Employee[]>("http://localhost:8080/api/admin/employees",{withCredentials : true});
    }

    PutEmployees(payload : EmployeeUpdateAdmin , id : number){
        return this.http.put(`http://localhost:8080/api/admin/employees/${id}`, payload ,{withCredentials : true , responseType: 'text'});
    }

    DeleteEmployees(id : number){
        return this.http.delete(`http://localhost:8080/api/admin/employees/${id}`,{withCredentials : true , responseType: 'text'})
    }
}
