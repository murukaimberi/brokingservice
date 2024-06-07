package za.co.afrikatek.brokingservice.config;

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

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(
                Object.class,
                Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries())
            )
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
            createCache(cm, za.co.afrikatek.brokingservice.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, za.co.afrikatek.brokingservice.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, za.co.afrikatek.brokingservice.domain.User.class.getName());
            createCache(cm, za.co.afrikatek.brokingservice.domain.Authority.class.getName());
            createCache(cm, za.co.afrikatek.brokingservice.domain.User.class.getName() + ".authorities");
            createCache(cm, za.co.afrikatek.brokingservice.domain.Address.class.getName());
            createCache(cm, za.co.afrikatek.brokingservice.domain.InsuranceType.class.getName());
            createCache(cm, za.co.afrikatek.brokingservice.domain.InsuranceType.class.getName() + ".classOfBusinesses");
            createCache(cm, za.co.afrikatek.brokingservice.domain.ClassOfBusiness.class.getName());
            createCache(cm, za.co.afrikatek.brokingservice.domain.ClassOfBusiness.class.getName() + ".subClassOfBusinesses");
            createCache(cm, za.co.afrikatek.brokingservice.domain.ClassOfBusiness.class.getName() + ".contracts");
            createCache(cm, za.co.afrikatek.brokingservice.domain.SubClassOfBusiness.class.getName());
            createCache(cm, za.co.afrikatek.brokingservice.domain.SubClassOfBusiness.class.getName() + ".contracts");
            createCache(cm, za.co.afrikatek.brokingservice.domain.Contract.class.getName());
            createCache(cm, za.co.afrikatek.brokingservice.domain.Contract.class.getName() + ".reinsurers");
            createCache(cm, za.co.afrikatek.brokingservice.domain.ContractReport.class.getName());
            createCache(cm, za.co.afrikatek.brokingservice.domain.Notification.class.getName());
            createCache(cm, za.co.afrikatek.brokingservice.domain.ContractHistory.class.getName());
            createCache(cm, za.co.afrikatek.brokingservice.domain.BusinessPartner.class.getName());
            createCache(cm, za.co.afrikatek.brokingservice.domain.BusinessPartner.class.getName() + ".awardedBusinessShares");
            createCache(cm, za.co.afrikatek.brokingservice.domain.BusinessPartner.class.getName() + ".addresses");
            createCache(cm, za.co.afrikatek.brokingservice.domain.BusinessPartner.class.getName() + ".insuredContracts");
            createCache(cm, za.co.afrikatek.brokingservice.domain.BusinessPartner.class.getName() + ".insurerContracts");
            createCache(cm, za.co.afrikatek.brokingservice.domain.BusinessPartner.class.getName() + ".brokerContracts");
            createCache(cm, za.co.afrikatek.brokingservice.domain.BusinessPartner.class.getName() + ".reInsurerContracts");
            createCache(cm, za.co.afrikatek.brokingservice.domain.Share.class.getName());
            createCache(cm, za.co.afrikatek.brokingservice.domain.ReinsurancePlacement.class.getName());
            createCache(cm, za.co.afrikatek.brokingservice.domain.Country.class.getName());
            createCache(cm, za.co.afrikatek.brokingservice.domain.Country.class.getName() + ".contracts");
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
