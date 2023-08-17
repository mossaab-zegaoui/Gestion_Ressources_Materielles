import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { MenuComponent } from './components/menu/menu.component';
import { HeadMenuComponent } from './components/head-menu/head-menu.component';
import { DepartementComponent } from './components/gestion-departements/departement/departement.component';
import { MembreComponent } from './components/gestion-departements/membre/membre.component';
import { FooterComponent } from './components/footer/footer.component';
import { BesoinComponent } from './components/gestionBesoin/besoin/besoin.component';
import { ListeBesoinComponent } from './components/gestionBesoin/liste-besoin/liste-besoin.component';
import { AppelOffreComponent } from './components/appel-offre/appel-offre.component';
import { OffreComponent } from './components/offre/offre.component';
import { OrdinateurComponent } from './components/gestionRessource/ordinateur/ordinateur.component';
import { ImprimanteComponent } from './components/gestionRessource/imprimante/imprimante.component';
import { SignalerPanneComponent } from './components/gestionPannes/signaler-panne/signaler-panne.component';
import { PannesMemberDepartementComponent } from './components/gestionPannes/pannes-member-departement/pannes-member-departement.component';
import { PannesTechnicienComponent } from './components/gestionPannes/pannes-technicien/pannes-technicien.component';
import { ConstatesResponsableComponent } from './components/gestionPannes/constates-responsable/constates-responsable.component';
import { TechnicienComponent } from './components/gestionPannes/technicien/technicien/technicien.component';
import { ForbiddenComponent } from './components/auth/forbidden/forbidden.component';
import { LoginComponent } from './components/auth/login/login.component';
import { LogoutComponent } from './components/auth/logout/logout.component';
import { PageNotFoundComponent } from './components/auth/page-not-found/page-not-found.component';
import { RegisterComponent } from './components/auth/register/register.component';
import { ServerErrorComponent } from './components/auth/server-error/server-error.component';
import { ToastComponent } from './components/toast/toast.component';
import { ToasterComponent } from './components/toaster/toaster.component';
import { AuthInterceptor } from './interceptor/auth.interceptor';
import { HomeComponent } from './components/Home/home/home.component';
import { ResetPasswordComponent } from './components/auth/reset-password/reset-password.component';
import { UpdatePasswordComponent } from './components/auth/update-password/update-password.component';
import { ConfirmRegistrationComponent } from './components/auth/confirm-registration/confirm-registration.component';
import { DepartementNomPipe } from './pipes/departement-nom.pipe';

@NgModule({
  declarations: [
    AppComponent,
    MenuComponent,
    HeadMenuComponent,
    DepartementComponent,
    MembreComponent,
    FooterComponent,
    BesoinComponent,
    ListeBesoinComponent,
    AppelOffreComponent,
    OffreComponent,
    OrdinateurComponent,
    ImprimanteComponent,
    SignalerPanneComponent,
    PannesMemberDepartementComponent,
    PannesTechnicienComponent,
    ConstatesResponsableComponent,
    TechnicienComponent,
    LoginComponent,
    RegisterComponent,
    ToastComponent,
    ToasterComponent,
    LogoutComponent,
    ServerErrorComponent,
    PageNotFoundComponent,
    ForbiddenComponent,
    HomeComponent,
    ResetPasswordComponent,
    UpdatePasswordComponent,
    ConfirmRegistrationComponent,
    DepartementNomPipe,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true,
    },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
