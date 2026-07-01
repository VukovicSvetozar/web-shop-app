import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PageNotFoundComponent } from './app-layout/page-not-found/page-not-found.component';

const routes: Routes = [

  {
    path: 'product',
    loadChildren: () => import('./product/product.module').then(mod => mod.ProductModule)
  },

  {
    path: 'support',
    loadChildren: () => import('./support/support.module').then(mod => mod.SupportModule)
  },

  {
    path: 'auth',
    loadChildren: () => import('./auth/auth.module').then(mod => mod.AuthModule)
  },

  {
    path: '',
    loadChildren: () => import('./product/product.module').then(mod => mod.ProductModule)
  },

  { path: '**', component: PageNotFoundComponent },


];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
