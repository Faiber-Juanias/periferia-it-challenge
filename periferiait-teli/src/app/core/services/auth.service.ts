import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { API_CONFIG } from '../config/api.config';
import { LoginRequest, LoginResponse } from '../models/auth.model';
import { UserProfile } from '../models/user.model';

/** Cliente HTTP del auth-service (login + perfil). */
@Injectable({ providedIn: 'root' })
export class AuthService {

  private readonly http = inject(HttpClient);

  login(request: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${API_CONFIG.authBaseUrl}/auth/login`, request);
  }

  getProfile(): Observable<UserProfile> {
    return this.http.get<UserProfile>(`${API_CONFIG.authBaseUrl}/users/me`);
  }
}
