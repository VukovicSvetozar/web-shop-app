import { NgModule } from '@angular/core';
import { Routes, RouterModule, mapToCanActivate } from '@angular/router';
import { SupportComponent } from './support/support.component';
import { MenuComponent } from '../app-layout/menu/menu.component';
import { GuardService } from 'src/app/services/guard.service';

const routes: Routes = [

    {
        path: '',
        component: SupportComponent,
        canActivate: mapToCanActivate([GuardService]),
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
export class SupportRoutingModule { }
