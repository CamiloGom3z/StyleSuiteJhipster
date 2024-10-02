import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'persona',
        data: { pageTitle: 'pruebaProyectoFinalApp.persona.home.title' },
        loadChildren: () => import('./persona/persona.module').then(m => m.PersonaModule),
      },
      {
        path: 'cargo',
        data: { pageTitle: 'pruebaProyectoFinalApp.cargo.home.title' },
        loadChildren: () => import('./cargo/cargo.module').then(m => m.CargoModule),
      },
      {
        path: 'empleado',
        data: { pageTitle: 'pruebaProyectoFinalApp.empleado.home.title' },
        loadChildren: () => import('./empleado/empleado.module').then(m => m.EmpleadoModule),
      },
      {
        path: 'cita',
        data: { pageTitle: 'pruebaProyectoFinalApp.cita.home.title' },
        loadChildren: () => import('./cita/cita.module').then(m => m.CitaModule),
      },
      {
        path: 'tipo-servcio',
        data: { pageTitle: 'pruebaProyectoFinalApp.tipoServcio.home.title' },
        loadChildren: () => import('./tipo-servcio/tipo-servcio.module').then(m => m.TipoServcioModule),
      },
      {
        path: 'servicio',
        data: { pageTitle: 'pruebaProyectoFinalApp.servicio.home.title' },
        loadChildren: () => import('./servicio/servicio.module').then(m => m.ServicioModule),
      },
      {
        path: 'agenda',
        data: { pageTitle: 'pruebaProyectoFinalApp.agenda.home.title' },
        loadChildren: () => import('./agenda/agenda.module').then(m => m.AgendaModule),
      },
      {
        path: 'agenda-empleado',
        data: { pageTitle: 'pruebaProyectoFinalApp.agendaEmpleado.home.title' },
        loadChildren: () => import('./agenda-empleado/agenda-empleado.module').then(m => m.AgendaEmpleadoModule),
      },
      {
        path: 'establecimiento',
        data: { pageTitle: 'pruebaProyectoFinalApp.establecimiento.home.title' },
        loadChildren: () => import('./establecimiento/establecimiento.module').then(m => m.EstablecimientoModule),
      },
      {
        path: 'productos',
        data: { pageTitle: 'pruebaProyectoFinalApp.productos.home.title' },
        loadChildren: () => import('./productos/productos.module').then(m => m.ProductosModule),
      },
      {
        path: 'categoria-producto',
        data: { pageTitle: 'pruebaProyectoFinalApp.categoriaProducto.home.title' },
        loadChildren: () => import('./categoria-producto/categoria-producto.module').then(m => m.CategoriaProductoModule),
      },
      {
        path: 'categoria-imagen',
        data: { pageTitle: 'pruebaProyectoFinalApp.categoriaImagen.home.title' },
        loadChildren: () => import('./categoria-imagen/categoria-imagen.module').then(m => m.CategoriaImagenModule),
      },
      {
        path: 'imagen',
        data: { pageTitle: 'pruebaProyectoFinalApp.imagen.home.title' },
        loadChildren: () => import('./imagen/imagen.module').then(m => m.ImagenModule),
      },
      {
        path: 'resenia',
        data: { pageTitle: 'pruebaProyectoFinalApp.resenia.home.title' },
        loadChildren: () => import('./resenia/resenia.module').then(m => m.ReseniaModule),
      },
      {
        path: 'promocion',
        data: { pageTitle: 'pruebaProyectoFinalApp.promocion.home.title' },
        loadChildren: () => import('./promocion/promocion.module').then(m => m.PromocionModule),
      },
      {
        path: 'pago',
        data: { pageTitle: 'pruebaProyectoFinalApp.pago.home.title' },
        loadChildren: () => import('./pago/pago.module').then(m => m.PagoModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
