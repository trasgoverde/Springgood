package com.dipassio.myapp.config;

import io.github.jhipster.config.JHipsterProperties;
import io.github.jhipster.config.cache.PrefixedKeyGenerator;
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
            createCache(cm, com.dipassio.myapp.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.dipassio.myapp.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.dipassio.myapp.domain.User.class.getName());
            createCache(cm, com.dipassio.myapp.domain.Authority.class.getName());
            createCache(cm, com.dipassio.myapp.domain.User.class.getName() + ".authorities");
            createCache(cm, com.dipassio.myapp.domain.Blog.class.getName());
            createCache(cm, com.dipassio.myapp.domain.Blog.class.getName() + ".appusers");
            createCache(cm, com.dipassio.myapp.domain.Blog.class.getName() + ".posts");
            createCache(cm, com.dipassio.myapp.domain.Post.class.getName());
            createCache(cm, com.dipassio.myapp.domain.Post.class.getName() + ".appusers");
            createCache(cm, com.dipassio.myapp.domain.Post.class.getName() + ".comments");
            createCache(cm, com.dipassio.myapp.domain.Post.class.getName() + ".proposals");
            createCache(cm, com.dipassio.myapp.domain.Post.class.getName() + ".tags");
            createCache(cm, com.dipassio.myapp.domain.Post.class.getName() + ".topics");
            createCache(cm, com.dipassio.myapp.domain.Topic.class.getName());
            createCache(cm, com.dipassio.myapp.domain.Topic.class.getName() + ".posts");
            createCache(cm, com.dipassio.myapp.domain.Tag.class.getName());
            createCache(cm, com.dipassio.myapp.domain.Tag.class.getName() + ".posts");
            createCache(cm, com.dipassio.myapp.domain.Comment.class.getName());
            createCache(cm, com.dipassio.myapp.domain.Comment.class.getName() + ".appusers");
            createCache(cm, com.dipassio.myapp.domain.Cmessage.class.getName());
            createCache(cm, com.dipassio.myapp.domain.Cmessage.class.getName() + ".senders");
            createCache(cm, com.dipassio.myapp.domain.Cmessage.class.getName() + ".receivers");
            createCache(cm, com.dipassio.myapp.domain.Bmessage.class.getName());
            createCache(cm, com.dipassio.myapp.domain.Notification.class.getName());
            createCache(cm, com.dipassio.myapp.domain.Notification.class.getName() + ".appusers");
            createCache(cm, com.dipassio.myapp.domain.Appphoto.class.getName());
            createCache(cm, com.dipassio.myapp.domain.Appprofile.class.getName());
            createCache(cm, com.dipassio.myapp.domain.Community.class.getName());
            createCache(cm, com.dipassio.myapp.domain.Community.class.getName() + ".appusers");
            createCache(cm, com.dipassio.myapp.domain.Community.class.getName() + ".blogs");
            createCache(cm, com.dipassio.myapp.domain.Community.class.getName() + ".csenders");
            createCache(cm, com.dipassio.myapp.domain.Community.class.getName() + ".creceivers");
            createCache(cm, com.dipassio.myapp.domain.Community.class.getName() + ".cfolloweds");
            createCache(cm, com.dipassio.myapp.domain.Community.class.getName() + ".cfollowings");
            createCache(cm, com.dipassio.myapp.domain.Community.class.getName() + ".cblockedusers");
            createCache(cm, com.dipassio.myapp.domain.Community.class.getName() + ".cblockingusers");
            createCache(cm, com.dipassio.myapp.domain.Community.class.getName() + ".cinterests");
            createCache(cm, com.dipassio.myapp.domain.Community.class.getName() + ".cactivities");
            createCache(cm, com.dipassio.myapp.domain.Community.class.getName() + ".ccelebs");
            createCache(cm, com.dipassio.myapp.domain.Follow.class.getName());
            createCache(cm, com.dipassio.myapp.domain.Follow.class.getName() + ".followeds");
            createCache(cm, com.dipassio.myapp.domain.Follow.class.getName() + ".followings");
            createCache(cm, com.dipassio.myapp.domain.Blockuser.class.getName());
            createCache(cm, com.dipassio.myapp.domain.Blockuser.class.getName() + ".blockedusers");
            createCache(cm, com.dipassio.myapp.domain.Blockuser.class.getName() + ".blockingusers");
            createCache(cm, com.dipassio.myapp.domain.Album.class.getName());
            createCache(cm, com.dipassio.myapp.domain.Album.class.getName() + ".appusers");
            createCache(cm, com.dipassio.myapp.domain.Album.class.getName() + ".photos");
            createCache(cm, com.dipassio.myapp.domain.Calbum.class.getName());
            createCache(cm, com.dipassio.myapp.domain.Calbum.class.getName() + ".communities");
            createCache(cm, com.dipassio.myapp.domain.Calbum.class.getName() + ".photos");
            createCache(cm, com.dipassio.myapp.domain.Photo.class.getName());
            createCache(cm, com.dipassio.myapp.domain.Interest.class.getName());
            createCache(cm, com.dipassio.myapp.domain.Interest.class.getName() + ".appusers");
            createCache(cm, com.dipassio.myapp.domain.Activity.class.getName());
            createCache(cm, com.dipassio.myapp.domain.Activity.class.getName() + ".appusers");
            createCache(cm, com.dipassio.myapp.domain.Celeb.class.getName());
            createCache(cm, com.dipassio.myapp.domain.Celeb.class.getName() + ".appusers");
            createCache(cm, com.dipassio.myapp.domain.Cinterest.class.getName());
            createCache(cm, com.dipassio.myapp.domain.Cinterest.class.getName() + ".communities");
            createCache(cm, com.dipassio.myapp.domain.Cactivity.class.getName());
            createCache(cm, com.dipassio.myapp.domain.Cactivity.class.getName() + ".communities");
            createCache(cm, com.dipassio.myapp.domain.Cceleb.class.getName());
            createCache(cm, com.dipassio.myapp.domain.Cceleb.class.getName() + ".communities");
            createCache(cm, com.dipassio.myapp.domain.Vtopic.class.getName());
            createCache(cm, com.dipassio.myapp.domain.Vtopic.class.getName() + ".appusers");
            createCache(cm, com.dipassio.myapp.domain.Vtopic.class.getName() + ".vquestions");
            createCache(cm, com.dipassio.myapp.domain.Vquestion.class.getName());
            createCache(cm, com.dipassio.myapp.domain.Vquestion.class.getName() + ".appusers");
            createCache(cm, com.dipassio.myapp.domain.Vquestion.class.getName() + ".vanswers");
            createCache(cm, com.dipassio.myapp.domain.Vquestion.class.getName() + ".vthumbs");
            createCache(cm, com.dipassio.myapp.domain.Vanswer.class.getName());
            createCache(cm, com.dipassio.myapp.domain.Vanswer.class.getName() + ".appusers");
            createCache(cm, com.dipassio.myapp.domain.Vanswer.class.getName() + ".vthumbs");
            createCache(cm, com.dipassio.myapp.domain.Vthumb.class.getName());
            createCache(cm, com.dipassio.myapp.domain.Vthumb.class.getName() + ".appusers");
            createCache(cm, com.dipassio.myapp.domain.Proposal.class.getName());
            createCache(cm, com.dipassio.myapp.domain.Proposal.class.getName() + ".appusers");
            createCache(cm, com.dipassio.myapp.domain.Proposal.class.getName() + ".proposalVotes");
            createCache(cm, com.dipassio.myapp.domain.ProposalVote.class.getName());
            createCache(cm, com.dipassio.myapp.domain.ProposalVote.class.getName() + ".appusers");
            createCache(cm, com.dipassio.myapp.domain.Appuser.class.getName());
            createCache(cm, com.dipassio.myapp.domain.Appuser.class.getName() + ".interests");
            createCache(cm, com.dipassio.myapp.domain.Appuser.class.getName() + ".activities");
            createCache(cm, com.dipassio.myapp.domain.Appuser.class.getName() + ".celebs");
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache == null) {
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
