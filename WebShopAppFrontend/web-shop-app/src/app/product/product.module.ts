import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomeComponent } from './home/home.component';
import { AppLayoutModule } from '../app-layout/app-layout.module';
import { ProductRoutingModule } from './product-routing.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AppMaterialModule } from '../app-material/app-material.module';
import { MainComponent } from './offer/main/main.component';
import { CategoryModalComponent } from './offer/category-modal/category-modal.component';
import { ImageModalComponent } from './offer/image-modal/image-modal.component';
import { SpecificationModalComponent } from './offer/specification-modal/specification-modal.component';
import { FeatureModalComponent } from './offer/feature-modal/feature-modal.component';
import { ProductDetailsComponent } from './product-details/product-details.component';
import { PurchaseModalComponent } from './purchase-modal/purchase-modal.component';
import { CommentModalComponent } from './comment-modal/comment-modal.component';

@NgModule({
  declarations: [
    HomeComponent,
    MainComponent,
    CategoryModalComponent,
    ImageModalComponent,
    SpecificationModalComponent,
    FeatureModalComponent,
    ProductDetailsComponent,
    PurchaseModalComponent,
    CommentModalComponent
  ],
  imports: [
    CommonModule,
    AppLayoutModule,
    AppMaterialModule,
    ProductRoutingModule,
    FormsModule,
    ReactiveFormsModule
  ]
})
export class ProductModule { }
