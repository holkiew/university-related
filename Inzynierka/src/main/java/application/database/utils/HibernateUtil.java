package application.database.utils;

import application.database.entities.*;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class HibernateUtil {
    //Annotation based configuration
    private static SessionFactory sessionAnnotationFactory;

    private static SessionFactory buildSessionAnnotationFactory() {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            Configuration configuration = new Configuration();
            configuration.configure("hibernate-annotation.cfg.xml");

            // Add all annotated entities
            configuration.addAnnotatedClass(AccountStatusEntity.class);
            configuration.addAnnotatedClass(AccountTypeEntity.class);
            configuration.addAnnotatedClass(AccountTemporaryDataEntity.class);
            configuration.addAnnotatedClass(AuctionDataEntity.class);
            configuration.addAnnotatedClass(AuctionEntity.class);
            configuration.addAnnotatedClass(AuctionCommodityPropertiesEntity.class);
            configuration.addAnnotatedClass(BidsEntity.class);
            configuration.addAnnotatedClass(CategoryEntity.class);
            configuration.addAnnotatedClass(CompanyDataEntity.class);
            configuration.addAnnotatedClass(CompanyEntity.class);
            configuration.addAnnotatedClass(ImageEntity.class);
            configuration.addAnnotatedClass(LanguageEntity.class);
            configuration.addAnnotatedClass(LocationToEntity.class);
            configuration.addAnnotatedClass(LocationFromEntity.class);
            configuration.addAnnotatedClass(StatusTypeEntity.class);
            configuration.addAnnotatedClass(UserDataEntity.class);
            configuration.addAnnotatedClass(UserEntity.class);
            configuration.addAnnotatedClass(TokenAccessEntity.class);
            configuration.addAnnotatedClass(TokenRefreshEntity.class);

            System.out.println("Hibernate Annotation Configuration loaded");
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties()).build();
            System.out.println("Hibernate Annotation serviceRegistry created");

            SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);

            return sessionFactory;
        }
        catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionAnnotationFactory() {
        if(sessionAnnotationFactory == null) sessionAnnotationFactory = buildSessionAnnotationFactory();
        return sessionAnnotationFactory;
    }


}