import { NgModule } from '@angular/core';
import { Routes, RouterModule, mapToCanActivate } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { MenuComponent } from '../app-layout/menu/menu.component';
import { MainComponent } from './offer/main/main.component';
import { ProductDetailsComponent } from './product-details/product-details.component';
import { GuardService } from '../services/guard.service';

const routes: Routes = [

    {
        path: '',
        component: HomeComponent,
        children: [
            {
                path: '',
                component: MenuComponent,
            }
        ]
    },
    {
        path: 'home',
        component: HomeComponent,
        children: [
            {
                path: '',
                component: MenuComponent,
            }
        ]
    },
    {
        path: 'offer',
        component: MainComponent,
        canActivate: mapToCanActivate([GuardService]),
        children: [
            {
                path: '',
                component: MenuComponent,
            }
        ]
    },
    {
        path: 'details/:id',
        component: ProductDetailsComponent,
        children: [
            {
                path: '',
                component: MenuComponent,
            }
        ]
    }

];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class ProductRoutingModule { }
