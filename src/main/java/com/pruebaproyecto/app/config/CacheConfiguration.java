package com.pruebaproyecto.app.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                    .build()
            );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.pruebaproyecto.app.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.pruebaproyecto.app.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.pruebaproyecto.app.domain.User.class.getName());
            createCache(cm, com.pruebaproyecto.app.domain.Authority.class.getName());
            createCache(cm, com.pruebaproyecto.app.domain.User.class.getName() + ".authorities");
            createCache(cm, com.pruebaproyecto.app.domain.Persona.class.getName());
            createCache(cm, com.pruebaproyecto.app.domain.Persona.class.getName() + ".citas");
            createCache(cm, com.pruebaproyecto.app.domain.Cargo.class.getName());
            createCache(cm, com.pruebaproyecto.app.domain.Empleado.class.getName());
            createCache(cm, com.pruebaproyecto.app.domain.Empleado.class.getName() + ".cargos");
            createCache(cm, com.pruebaproyecto.app.domain.Empleado.class.getName() + ".agendaEmpleados");
            createCache(cm, com.pruebaproyecto.app.domain.Cita.class.getName());
            createCache(cm, com.pruebaproyecto.app.domain.Cita.class.getName() + ".servicios");
            createCache(cm, com.pruebaproyecto.app.domain.Cita.class.getName() + ".pagos");
            createCache(cm, com.pruebaproyecto.app.domain.TipoServcio.class.getName());
            createCache(cm, com.pruebaproyecto.app.domain.Servicio.class.getName());
            createCache(cm, com.pruebaproyecto.app.domain.Servicio.class.getName() + ".resenias");
            createCache(cm, com.pruebaproyecto.app.domain.Servicio.class.getName() + ".promociones");
            createCache(cm, com.pruebaproyecto.app.domain.Agenda.class.getName());
            createCache(cm, com.pruebaproyecto.app.domain.AgendaEmpleado.class.getName());
            createCache(cm, com.pruebaproyecto.app.domain.Establecimiento.class.getName());
            createCache(cm, com.pruebaproyecto.app.domain.Establecimiento.class.getName() + ".agendas");
            createCache(cm, com.pruebaproyecto.app.domain.Establecimiento.class.getName() + ".empleados");
            createCache(cm, com.pruebaproyecto.app.domain.Establecimiento.class.getName() + ".categoriasProductos");
            createCache(cm, com.pruebaproyecto.app.domain.Establecimiento.class.getName() + ".categoriasImagens");
            createCache(cm, com.pruebaproyecto.app.domain.Productos.class.getName());
            createCache(cm, com.pruebaproyecto.app.domain.CategoriaProducto.class.getName());
            createCache(cm, com.pruebaproyecto.app.domain.CategoriaImagen.class.getName());
            createCache(cm, com.pruebaproyecto.app.domain.Imagen.class.getName());
            createCache(cm, com.pruebaproyecto.app.domain.Resenia.class.getName());
            createCache(cm, com.pruebaproyecto.app.domain.Promocion.class.getName());
            createCache(cm, com.pruebaproyecto.app.domain.Promocion.class.getName() + ".servicios");
            createCache(cm, com.pruebaproyecto.app.domain.Pago.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
