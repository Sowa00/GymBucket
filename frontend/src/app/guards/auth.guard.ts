import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { map, take } from 'rxjs/operators';
import { AuthService } from '../services/auth.service';

@Injectable({
    providedIn: 'root'
})
export class AuthGuard implements CanActivate {
    constructor(
        private authService: AuthService,
        private router: Router
    ) {}

    canActivate(
        route: ActivatedRouteSnapshot,
        state: RouterStateSnapshot
    ): Observable<boolean> | Promise<boolean> | boolean {
        return this.authService.isAuthenticated$.pipe(
            take(1),
            map(isAuthenticated => {
                if (isAuthenticated) {
                    return true;
                } else {
                    // Store the attempted URL for redirecting after login
                    this.router.navigate(['/login'], {
                        queryParams: { returnUrl: state.url }
                    });
                    return false;
                }
            })
        );
    }
}

@Injectable({
    providedIn: 'root'
})
export class GuestGuard implements CanActivate {
    constructor(
        private authService: AuthService,
        private router: Router
    ) {}

    canActivate(): Observable<boolean> | Promise<boolean> | boolean {
        return this.authService.isAuthenticated$.pipe(
            take(1),
            map(isAuthenticated => {
                if (isAuthenticated) {
                    this.router.navigate(['/homepage']);
                    return false;
                } else {
                    return true;
                }
            })
        );
    }
}

@Injectable({
    providedIn: 'root'
})
export class RoleGuard implements CanActivate {
    constructor(
        private authService: AuthService,
        private router: Router
    ) {}

    canActivate(
        route: ActivatedRouteSnapshot,
        state: RouterStateSnapshot
    ): Observable<boolean> | Promise<boolean> | boolean {
        const expectedRoles = route.data['roles'] as Array<string>;

        return this.authService.currentUser$.pipe(
            take(1),
            map(user => {
                if (!user) {
                    this.router.navigate(['/login']);
                    return false;
                }

                if (expectedRoles && expectedRoles.length > 0) {
                    const hasRole = expectedRoles.includes(user.role);
                    if (!hasRole) {
                        this.router.navigate(['/homepage']); // or unauthorized page
                        return false;
                    }
                }

                return true;
            })
        );
    }
}
