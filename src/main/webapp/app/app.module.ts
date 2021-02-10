import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { SpringgoodSharedModule } from 'app/shared/shared.module';
import { SpringgoodCoreModule } from 'app/core/core.module';
import { SpringgoodAppRoutingModule } from './app-routing.module';
import { SpringgoodHomeModule } from './home/home.module';
import { SpringgoodEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ActiveMenuDirective } from './layouts/navbar/active-menu.directive';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    SpringgoodSharedModule,
    SpringgoodCoreModule,
    SpringgoodHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    SpringgoodEntityModule,
    SpringgoodAppRoutingModule,
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, ActiveMenuDirective, FooterComponent],
  bootstrap: [MainComponent],
})
export class SpringgoodAppModule {}
