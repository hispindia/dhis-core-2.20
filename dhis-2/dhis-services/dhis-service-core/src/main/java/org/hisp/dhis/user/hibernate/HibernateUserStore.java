package org.hisp.dhis.user.hibernate;

/*
 * Copyright (c) 2004-2014, University of Oslo
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * Neither the name of the HISP project nor the names of its contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hisp.dhis.common.IdentifiableObjectUtils;
import org.hisp.dhis.common.hibernate.HibernateIdentifiableObjectStore;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.system.util.SqlHelper;
import org.hisp.dhis.user.User;
import org.hisp.dhis.user.UserStore;

/**
 * @author Nguyen Hong Duc
 */
public class HibernateUserStore
    extends HibernateIdentifiableObjectStore<User>
    implements UserStore
{
    // -------------------------------------------------------------------------
    // UserStore implementation
    // -------------------------------------------------------------------------

    @Override
    @SuppressWarnings("unchecked")
    public List<User> getAllOrderedName( int first, int max )
    {
        Criteria criteria = getCriteria();
        criteria.addOrder( Order.asc( "surname" ) ).addOrder( Order.asc( "firstName" ) );
        criteria.setFirstResult( first );
        criteria.setMaxResults( max );
        return criteria.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> getAllLikeName( String name, int first, int max )
    {
        Criteria criteria = getCriteria();
        criteria.add( Restrictions.or( Restrictions.ilike( "surname", "%" + name + "%" ),
            Restrictions.ilike( "firstName", "%" + name + "%" ) ) );
        criteria.addOrder( Order.asc( "surname" ) ).addOrder( Order.asc( "firstName" ) );
        criteria.setFirstResult( first );
        criteria.setMaxResults( max );
        return criteria.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> getUsersByPhoneNumber( String phoneNumber )
    {
        String hql = "from User u where u.phoneNumber = :phoneNumber";

        Query query = sessionFactory.getCurrentSession().createQuery( hql );
        query.setString( "phoneNumber", phoneNumber );

        return query.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> getUsersByName( String name )
    {
        Criteria criteria = getCriteria();
        criteria.add( Restrictions.or( Restrictions.ilike( "surname", "%" + name + "%" ),
            Restrictions.ilike( "firstName", "%" + name + "%" ) ) );
        criteria.addOrder( Order.asc( "surname" ) ).addOrder( Order.asc( "firstName" ) );

        return criteria.list();
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public List<User> getManagedUsersBetween( String searchKey, User user, 
        boolean constrainManagedGroups, boolean constrainAuthSubset, 
        Date inactiveSince, boolean selfRegistered, OrganisationUnit organisationUnit, Integer first, Integer max )
    {
        SqlHelper hlp = new SqlHelper();
        
        String hql = 
            "select distinct u from User u " +
            "inner join u.userCredentials uc " +
            "left join u.groups g ";

        if ( searchKey != null )
        {
            hql += hlp.whereAnd() + " (" +
                "lower(u.firstName) like :key " +
                "or lower(u.surname) like :key " +
                "or lower(uc.username) like :key) ";
        }
        
        if ( constrainManagedGroups )
        {
            hql += hlp.whereAnd() + " g.id in (:ids) ";
        }
        
        if ( constrainAuthSubset )
        {
            hql += hlp.whereAnd() + " not exists (" +
                "select uc2 from UserCredentials uc2 " +
                "inner join uc2.userAuthorityGroups ag " +
                "inner join ag.authorities a " +
                "where uc2.id = uc.id " +
                "and a not in (:auths) ) ";
        }
        
        //TODO constrain by own user roles

        if ( inactiveSince != null )
        {
            hql += hlp.whereAnd() + " uc.lastLogin < :inactiveSince ";
        }
        
        if ( selfRegistered )
        {
            hql += hlp.whereAnd() + " uc.selfRegistered = true ";
        }
        
        if ( organisationUnit != null )
        {
            hql += hlp.whereAnd() + " :organisationUnit in elements(u.organisationUnits) ";
        }
        
        hql += "order by u.surname, u.firstName";
        
        Query query = sessionFactory.getCurrentSession().createQuery( hql );
        
        if ( searchKey != null )
        {
            query.setString( "key", "%" + searchKey.toLowerCase() + "%" );
        }
        
        if ( constrainManagedGroups )
        {
            Collection<Integer> managedGroups = IdentifiableObjectUtils.getIdentifiers( user.getManagedGroups() );

            query.setParameterList( "ids", managedGroups );
        }
        
        if ( constrainAuthSubset )
        {
            Set<String> auths = user.getUserCredentials().getAllAuthorities();
            
            query.setParameterList( "auths", auths );
        }
        
        if ( inactiveSince != null )
        {
            query.setDate( "inactiveSince", inactiveSince );
        }
        
        if ( organisationUnit != null )
        {
            query.setEntity( "organisationUnit", organisationUnit );
        }
        
        if ( first != null )
        {
            query.setFirstResult( first );
        }
        
        if ( max != null )
        {
            query.setMaxResults( max ).list();
        }
        
        return query.list();
    }
}
