import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SupportComponent } from './support/support.component';
import { SupportRoutingModule } from './support-routing.module';
import { AppLayoutModule } from '../app-layout/app-layout.module';
import { AppMaterialModule } from '../app-material/app-material.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

@NgModule({
  declarations: [
    SupportComponent
  ],
  imports: [
    CommonModule,
    AppLayoutModule,
    SupportRoutingModule,
    AppMaterialModule,
    FormsModule,
    ReactiveFormsModule
  ]
})
export class SupportModule { }
