package vn.fptu.reasbe.model.entity.core;

import jakarta.persistence.PrePersist;
import vn.fptu.reasbe.model.enums.core.StatusEntity;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import vn.fptu.reasbe.utils.common.DateUtils;

/**
 *
 * @author ntig
 */
@MappedSuperclass
@Access(AccessType.FIELD)
public class AbstractEntity {

    protected static String getRawClassName(Class<?> baseClass) {
        Class<?> clazz = baseClass;
        while (isSyntheticClass(clazz)) {
            clazz = clazz.getSuperclass();
            if (clazz == null || clazz.equals(Object.class) || clazz.equals(AbstractEntity.class)) {
                throw new IllegalArgumentException(String.format("Can't retrieve non-synthetic class from class %s", baseClass));
            }
        }

        return clazz.getName();
    }

    protected static boolean isSyntheticClass(Class<?> clazz) {
        return clazz.isSynthetic()
                || org.hibernate.proxy.HibernateProxy.class.isAssignableFrom(clazz)
                || java.lang.reflect.Proxy.class.isAssignableFrom(clazz)
                || org.springframework.cglib.proxy.Proxy.class.isAssignableFrom(clazz);
    }

    @Transient
    private boolean transientHashCodeLeaked = false;

    @Transient
    private String rawClassName = getRawClassName(getClass());

    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    @Access(AccessType.PROPERTY) // to avoid unnecessary proxy initialisation when calling getId()
    protected Integer id;

    @NotNull
    @Column(name = "STATUS_ENTITY")
    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    protected StatusEntity statusEntity;

    @PrePersist
    protected void onCreate() {
        setStatusEntity(StatusEntity.ACTIVE);
    }

    public boolean isPersisted() {
        return this.id != null;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (obj instanceof AbstractEntity) {
            final AbstractEntity other = (AbstractEntity) obj;
            if (isPersisted() && other.isPersisted()) { // both entities are not new

                // Because entities are currently used in clientside, we cannot use HibernateProxyHelper here >
                // we cannot compare class for sure they are the same class, just compare ID.
                return getId().equals(other.getId()) && rawClassName.equals(other.rawClassName);
            }
            // if one of entity is new (transient), they are considered not equal.
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        if (!isPersisted()) { // is new or is in transient state.
            transientHashCodeLeaked = true;
            return -super.hashCode();
        }

        // because hashcode has just been asked for when the object is in transient state
        // at that time, super.hashCode(); is returned. Now for consistency, we return the
        // same value.
        if (transientHashCodeLeaked) {
            return -super.hashCode();
        }
        return getId().hashCode();
        // The above mechanism obey the rule: if 2 objects are equal, their hashcode must be same.
    }
}
