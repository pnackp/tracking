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