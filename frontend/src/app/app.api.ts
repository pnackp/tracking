import { Injectable , signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';

export interface LoginInterface {
  identifier: string;
  password: string;
}

export interface RegisterInterface {
  employee_id: string,
  first_name: string,
  last_name: string,
  password: string,
  email: string,
  phone_number: string
}

export interface TruckInfo {
  id: number;
  lat: number;
  lng: number;
  temp_in: number;
  temp_out: number;
  pressure: number;
}

export interface Message {
  id: number;
}

export interface Payload {
  truckinfo: TruckInfo;
  truck_msg: Message;
}

@Injectable({ providedIn: 'root' })
export class ApiService {

  constructor(private http: HttpClient) { }

  login(payload: LoginInterface) {
    return this.http.post("http://localhost:8080/login", payload, { withCredentials: true });
  }
  register(payload: RegisterInterface) {
    return this.http.post("http://localhost:8080/register", payload, { withCredentials: true });
  }
}

@Injectable({
  providedIn: 'root',
})
export class WebsocketService {

  private socket!: WebSocket;

  payloadList = signal<Payload[]>([]);
  payloadLength = signal(0);

  connect() {
    this.socket = new WebSocket('ws://localhost:8080/ws');

    this.socket.addEventListener('open', () => {
      console.log('WebSocket Connected');
    });

    this.socket.addEventListener('message', (event) => {
      try {
        const data: Payload = JSON.parse(event.data);

  
        const current = this.payloadList();
        const index = current.findIndex(
          p => p.truckinfo.id === data.truckinfo.id
        );

        if (index !== -1) {
          current[index] = data;
        } else {
          current.push(data);
        }

        this.payloadList.set([...current]);
        this.payloadLength.set(current.length);

      } catch (err) {
        console.error('Invalid JSON:', err);
      }
    });

    this.socket.addEventListener('close', () => {
      console.log('WebSocket Closed');
    });

    this.socket.addEventListener('error', (err) => {
      console.error('WebSocket Error:', err);
    });
  }

  disconnect() {
    this.socket?.close();
  }
}