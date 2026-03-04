import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { LoginPayload, RegisterRequest } from './app.dto';

@Injectable({ providedIn: 'root' })
export class ApiComponent {
    constructor(private http: HttpClient) { }

    LoginRequest(data: LoginPayload) {
        return this.http.post("http://localhost:8080/api/auth/login",data,{withCredentials: true});
    }

    RegisterRequest(data : RegisterRequest){
        return this.http.post("http://localhost:8080/api/auth/register",data,{withCredentials : true})
    }

    LogoutRequest(){
        return this.http.get("http://localhost:8080/api/auth/logout",{withCredentials : true , responseType: 'text'});
    }
}
