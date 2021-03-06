package org.openmrs.module.shr.odd.api.db.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.openmrs.Auditable;
import org.openmrs.Concept;
import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.Order;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.shr.odd.api.db.OnDemandDocumentDAO;
import org.openmrs.module.shr.odd.model.OnDemandDocumentEncounterLink;
import org.openmrs.module.shr.odd.model.OnDemandDocumentRegistration;
import org.openmrs.module.shr.odd.model.OnDemandDocumentType;
import org.springframework.transaction.annotation.Transactional;

/**
 * On-demand document DAO class
 */
@Transactional(readOnly = true)
public class HibernateOnDemandDocumentDAO implements OnDemandDocumentDAO {
	
	// Hibernate session factory
	private DbSessionFactory m_sessionFactory;
	
	/**
	 * Save an on demand document registration entry
	 * @see org.openmrs.module.shr.odd.api.db.OnDemandDocumentDAO#saveOnDemandDocumentRegistration(org.openmrs.module.shr.odd.model.OnDemandDocumentRegistration)
	 */
	@Override
	public OnDemandDocumentRegistration saveOnDemandDocumentRegistration(OnDemandDocumentRegistration document) {
		this.updateAuditableProperties(document);
		this.m_sessionFactory.getCurrentSession().saveOrUpdate(document);
		return document;
	}
	
	/**
	 * Update auditable object properties
	 */
	private void updateAuditableProperties(Auditable data) {
		if(data.getUuid() == null)
			data.setUuid(UUID.randomUUID().toString());
		if(data.getCreator() == null)
		{
			data.setCreator(Context.getAuthenticatedUser());
			data.setDateCreated(new Date());
		}
		data.setDateChanged(new Date());
    }

	/**
	 * Get an on demand document registration by ID
	 * @see org.openmrs.module.shr.odd.api.db.OnDemandDocumentDAO#getOnDemandDocumentRegistrationById(java.lang.Integer)
	 */
	@Override
	public OnDemandDocumentRegistration getOnDemandDocumentRegistrationById(Integer id) {
		return (OnDemandDocumentRegistration)this.m_sessionFactory.getCurrentSession().get(OnDemandDocumentRegistration.class, id);
	}
	
	/**
	 * Get on demand document registration by UUID
	 * @see org.openmrs.module.shr.odd.api.db.OnDemandDocumentDAO#getOnDemandDocumentRegistrationByUuid(java.lang.String)
	 */
	@Override
	public OnDemandDocumentRegistration getOnDemandDocumentRegistrationByUuid(String uuid) {
		return this.getClassByUuid(OnDemandDocumentRegistration.class, uuid);
	}
	
	/**
	 * Get ODD by accession number 
	 * @see org.openmrs.module.shr.odd.api.db.OnDemandDocumentDAO#getOnDemandDocumentRegistrationByAccessionNumber(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
    @Override
	public List<OnDemandDocumentRegistration> getOnDemandDocumentRegistrationsByAccessionNumber(String accessionNumber, boolean includeVoided) {
		Criteria crit = this.m_sessionFactory.getCurrentSession().createCriteria(OnDemandDocumentRegistration.class)
				.add(Restrictions.eq("accessionNumber", accessionNumber));
		if(!includeVoided)
				crit.add(Restrictions.eq("voided", includeVoided));
		return (List<OnDemandDocumentRegistration>)crit.list();
	}

	/**
	 * Get on-demand document registration
	 * @see org.openmrs.module.shr.odd.api.db.OnDemandDocumentDAO#getOnDemandDocumentRegistrationsByPatient(org.openmrs.Patient)
	 */
	@SuppressWarnings("unchecked")
    @Override
	public List<OnDemandDocumentRegistration> getOnDemandDocumentRegistrationsByPatient(Patient patient, boolean includeVoided) {
		Criteria crit = this.m_sessionFactory.getCurrentSession().createCriteria(OnDemandDocumentRegistration.class)
				.add(Restrictions.eq("patient", patient));
		
		if(!includeVoided)
			crit.add(Restrictions.eq("voided", includeVoided));
		return (List<OnDemandDocumentRegistration>)crit.list();
	}
	
	/**
	 * Get on-demand document type by id
	 * @see org.openmrs.module.shr.odd.api.db.OnDemandDocumentDAO#getOnDemandDocumentTypeById(java.lang.Integer)
	 */
	@Override
	public OnDemandDocumentType getOnDemandDocumentTypeById(Integer id) {
		return (OnDemandDocumentType) this.m_sessionFactory.getCurrentSession().get(OnDemandDocumentType.class, id);
	}
	
	/**
	 * Get an on-demand document type by UUID
	 * @see org.openmrs.module.shr.odd.api.db.OnDemandDocumentDAO#getOnDemandDocumentTypeByUuid(java.lang.String)
	 */
	@Override
	public OnDemandDocumentType getOnDemandDocumentTypeByUuid(String uuid) {
		return this.getClassByUuid(OnDemandDocumentType.class, uuid);
	}
	
	/**
	 * Save an on-demand document type
	 * @see org.openmrs.module.shr.odd.api.db.OnDemandDocumentDAO#saveOnDemandDocumentType(org.openmrs.module.shr.odd.model.OnDemandDocumentType)
	 */
	@Override
	public OnDemandDocumentType saveOnDemandDocumentType(OnDemandDocumentType documentType) {
		this.updateAuditableProperties(documentType);
		this.m_sessionFactory.getCurrentSession().saveOrUpdate(documentType);
		return documentType;
	}
	
	/**
	 * Get all links for a document encounter
	 * @see org.openmrs.module.shr.odd.api.db.OnDemandDocumentDAO#getOnDemandDocumentEncounterLinks(org.openmrs.module.shr.odd.model.OnDemandDocumentRegistration)
	 */
	@SuppressWarnings("unchecked")
    @Override
	public List<OnDemandDocumentEncounterLink> getOnDemandDocumentEncounterLinks(OnDemandDocumentRegistration document, boolean includeVoided) {
		Criteria crit = this.m_sessionFactory.getCurrentSession().createCriteria(OnDemandDocumentEncounterLink.class)
				.add(Restrictions.eq("registration", document));
		if(!includeVoided)
			crit.add(Restrictions.eq("voided", includeVoided));
		return (List<OnDemandDocumentEncounterLink>)crit.list();
	}
	
	/**
	 * Save an on demand document to encounter link
	 * @see org.openmrs.module.shr.odd.api.db.OnDemandDocumentDAO#saveOnDemandDocumentEncounterLink(org.openmrs.module.shr.odd.model.OnDemandDocumentEncounterLink)
	 */
	@Override
	public OnDemandDocumentEncounterLink saveOnDemandDocumentEncounterLink(OnDemandDocumentEncounterLink link) {
		this.updateAuditableProperties(link);
		this.m_sessionFactory.getCurrentSession().saveOrUpdate(link);
		return link;
	}

	
    /**
     * @param sessionFactory the sessionFactory to set
     */
    public void setSessionFactory(DbSessionFactory sessionFactory) {
    	this.m_sessionFactory = sessionFactory;
    }

    /**
     * Get class by UUID
     */
    @SuppressWarnings("unchecked")
    private <T> T getClassByUuid(Class<T> clazz, String uuid)
    {
    	return (T)this.m_sessionFactory.getCurrentSession().createCriteria(clazz).add(Restrictions.eq("uuid", uuid)).uniqueResult();
    }

    /**
     * Get obs group members
     */
	@Override
    public List<Obs> getObsGroupMembers(Obs containerObs) {
		if(containerObs == null)
			return new ArrayList<Obs>();
		Criteria crit = this.m_sessionFactory.getCurrentSession().createCriteria(Obs.class)
				.add(Restrictions.eq("obsGroup", containerObs)).add(Restrictions.eq("voided", false));
		return (List<Obs>)crit.list();
    }

    /**
     * Get obs group members
     */
	@Override
    public List<Obs> getObsGroupMembers(List<Obs> containerObs) {
		if(containerObs.size() == 0)
			return new ArrayList<Obs>();
		Criteria crit = this.m_sessionFactory.getCurrentSession().createCriteria(Obs.class)
				.add(Restrictions.in("obsGroup", containerObs));
		return (List<Obs>)crit.list();

    }
	
	@Override
    public List<Obs> getObsGroupMembers(List<Obs> containerObs, List<Concept> concept) {
		if(containerObs.size() == 0 || concept.size() == 0)
			return new ArrayList<Obs>();
		Criteria crit = this.m_sessionFactory.getCurrentSession().createCriteria(Obs.class)
				.add(Restrictions.in("obsGroup", containerObs))
				.add(Restrictions.in("concept", concept));
		return (List<Obs>)crit.list();
    }

	/**
	 * Get the list of orders associated with the specified encounters
	 * @see org.openmrs.module.shr.odd.api.db.OnDemandDocumentDAO#getEncounterOrders(java.util.List)
	 */
	@Override
    public List<Order> getEncounterOrders(List<Encounter> encounters) {
		if(encounters == null || encounters.size() == 0)
			return new ArrayList<Order>();
		Order o;
		
		Criteria crit = this.m_sessionFactory.getCurrentSession().createCriteria(Order.class)
				.add(Restrictions.in("encounter", encounters));
		return (List<Order>)crit.list();
    }

	/**
	 * Get encounter orders of the specified type
	 * @see org.openmrs.module.shr.odd.api.db.OnDemandDocumentDAO#getEncounterOrders(java.util.List, java.lang.Class)
	 */
	@Override
    public List<Order> getEncounterOrders(List<Encounter> encounters, Class<? extends Order> orderType) {
		if(encounters == null || encounters.size() == 0)
			return new ArrayList<Order>();
		Order o;
		
		Criteria crit = this.m_sessionFactory.getCurrentSession().createCriteria(orderType)
				.add(Restrictions.in("encounter", encounters));
		return (List<Order>)crit.list();
    }

	/**
	 * Get on-demand document registrations by patient and type
	 * @see org.openmrs.module.shr.odd.api.db.OnDemandDocumentDAO#getOnDemandDocumentRegistrationsByPatient(org.openmrs.Patient, java.lang.Class, boolean)
	 */
	@Override
    public List<OnDemandDocumentRegistration> getOnDemandDocumentRegistrationsByPatient(Patient patient,
                                                                                        OnDemandDocumentType documentType,
                                                                                        boolean includeVoided) {
		
		Criteria crit = this.m_sessionFactory.getCurrentSession().createCriteria(OnDemandDocumentRegistration.class)
				.add(Restrictions.eq("patient", patient))
				.add(Restrictions.eq("type", documentType));
		
		if(!includeVoided)
			crit.add(Restrictions.eq("voided", includeVoided));
		
		return (List<OnDemandDocumentRegistration>)crit.list();		

	}
}
