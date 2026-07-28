import { DatePipe } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { AuthStore } from '../../core/auth/auth.store';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-profile',
  imports: [DatePipe],
  templateUrl: './profile.html',
})
export class Profile implements OnInit {
  private readonly authService = inject(AuthService);
  private readonly authStore = inject(AuthStore);

  protected readonly user = this.authStore.user;

  ngOnInit(): void {
    // Si aún no está en el estado (p. ej. tras recargar la página), lo pedimos.
    if (!this.user()) {
      this.authService.getProfile().subscribe({
        next: (profile) => this.authStore.setUser(profile),
      });
    }
  }
}
