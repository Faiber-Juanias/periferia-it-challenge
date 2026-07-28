import { isPlatformBrowser } from '@angular/common';
import { PLATFORM_ID, computed, inject } from '@angular/core';
import {
  patchState,
  signalStore,
  withComputed,
  withHooks,
  withMethods,
  withState,
} from '@ngrx/signals';
import { UserProfile } from '../models/user.model';

const TOKEN_KEY = 'periferia.token';

interface AuthState {
  token: string | null;
  user: UserProfile | null;
}

const initialState: AuthState = {
  token: null,
  user: null,
};

/**
 * Estado global de autenticación (NgRx SignalStore, singleton providedIn root).
 * Expone signals (token, user) y un computed isAuthenticated. Persiste el token
 * en localStorage (solo en navegador) para sobrevivir recargas.
 */
export const AuthStore = signalStore(
  { providedIn: 'root' },
  withState(initialState),
  withComputed((store) => ({
    isAuthenticated: computed(() => !!store.token()),
  })),
  withMethods((store) => {
    const platformId = inject(PLATFORM_ID);
    const isBrowser = isPlatformBrowser(platformId);

    return {
      setToken(token: string): void {
        if (isBrowser) {
          localStorage.setItem(TOKEN_KEY, token);
        }
        patchState(store, { token });
      },
      setUser(user: UserProfile): void {
        patchState(store, { user });
      },
      logout(): void {
        if (isBrowser) {
          localStorage.removeItem(TOKEN_KEY);
        }
        patchState(store, initialState);
      },
    };
  }),
  withHooks({
    onInit(store) {
      const platformId = inject(PLATFORM_ID);
      if (isPlatformBrowser(platformId)) {
        const token = localStorage.getItem(TOKEN_KEY);
        if (token) {
          patchState(store, { token });
        }
      }
    },
  }),
);
