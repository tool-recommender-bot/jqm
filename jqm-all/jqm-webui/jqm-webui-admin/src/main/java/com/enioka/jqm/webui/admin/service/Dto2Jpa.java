package com.enioka.jqm.webui.admin.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import com.enioka.jqm.jpamodel.DeploymentParameter;
import com.enioka.jqm.jpamodel.GlobalParameter;
import com.enioka.jqm.jpamodel.JndiObjectResource;
import com.enioka.jqm.jpamodel.JndiObjectResourceParameter;
import com.enioka.jqm.jpamodel.JobDef;
import com.enioka.jqm.jpamodel.JobDefParameter;
import com.enioka.jqm.jpamodel.Node;
import com.enioka.jqm.jpamodel.Queue;
import com.enioka.jqm.webui.admin.dto.GlobalParameterDto;
import com.enioka.jqm.webui.admin.dto.JndiObjectResourceDto;
import com.enioka.jqm.webui.admin.dto.JobDefDto;
import com.enioka.jqm.webui.admin.dto.ParameterDto;
import com.enioka.jqm.webui.admin.dto.QueueDTO;
import com.enioka.jqm.webui.admin.dto.QueueMappingDTO;

class Dto2Jpa
{
    private Dto2Jpa()
    {

    }

    @SuppressWarnings("unchecked")
    static <J> J setJpa(Object dto, EntityManager em)
    {
        if (dto instanceof JobDefDto)
        {
            return (J) setJpa(em, (JobDefDto) dto);
        }
        else if (dto instanceof GlobalParameterDto)
        {
            return (J) setJpa(em, (GlobalParameterDto) dto);
        }
        else if (dto instanceof JndiObjectResourceDto)
        {
            return (J) setJpa(em, (JndiObjectResourceDto) dto);
        }
        else if (dto instanceof QueueMappingDTO)
        {
            return (J) setJpa(em, (QueueMappingDTO) dto);
        }
        else if (dto instanceof QueueDTO)
        {
            return (J) setJpa(em, (QueueDTO) dto);
        }
        return null;
    }

    private static GlobalParameter setJpa(EntityManager em, GlobalParameterDto dto)
    {
        GlobalParameter n = null;

        if (dto.getId() == null)
        {
            n = new GlobalParameter();
        }
        else
        {
            n = em.find(GlobalParameter.class, dto.getId());
        }

        // Update or set fields
        n.setKey(dto.getKey());
        n.setValue(dto.getValue());

        // save
        n = em.merge(n);

        // Done
        return n;
    }

    private static JobDef setJpa(EntityManager em, JobDefDto dto)
    {
        JobDef jpa = null;

        if (dto.getId() == null)
        {
            jpa = new JobDef();
        }
        else
        {
            jpa = em.find(JobDef.class, dto.getId());
        }

        jpa.setApplication(dto.getApplication());
        jpa.setApplicationName(dto.getApplicationName());
        jpa.setCanBeRestarted(dto.isCanBeRestarted());
        jpa.setDescription(dto.getDescription());
        jpa.setHighlander(dto.isHighlander());
        jpa.setJarPath(dto.getJarPath());
        jpa.setJavaClassName(dto.getJavaClassName());
        jpa.setKeyword1(dto.getKeyword1());
        jpa.setKeyword2(dto.getKeyword2());
        jpa.setKeyword3(dto.getKeyword3());
        jpa.setModule(dto.getModule());
        jpa.setQueue(em.find(Queue.class, dto.getQueueId()));

        jpa = em.merge(jpa);

        List<JobDefParameter> prmFromBefore = new ArrayList<JobDefParameter>();
        List<JobDefParameter> prmNow = new ArrayList<JobDefParameter>();
        for (JobDefParameter ee : jpa.getParameters())
        {
            prmFromBefore.add(ee);
        }

        for (ParameterDto p : dto.getParameters())
        {
            if (p.getKey() == null || p.getKey().isEmpty() || p.getValue() == null || p.getValue().isEmpty())
            {
                continue;
            }

            JobDefParameter np = null;
            if (p.getId() == null)
            {
                np = new JobDefParameter();
            }
            else
            {
                np = em.find(JobDefParameter.class, p.getId());
            }
            np.setKey(p.getKey());
            np.setValue(p.getValue());
            jpa.getParameters().add(np);
            np = em.merge(np);
            prmNow.add(np);
        }

        // Remove parameters that are not present anymore
        before: for (JobDefParameter presentbefore : prmFromBefore)
        {
            for (JobDefParameter stillhere : prmNow)
            {
                if (stillhere.getId() == presentbefore.getId())
                {
                    continue before;
                }
            }
            jpa.getParameters().remove(presentbefore);
            em.remove(presentbefore);
        }

        return jpa;
    }

    private static JndiObjectResource setJpa(EntityManager em, JndiObjectResourceDto dto)
    {
        JndiObjectResource jpa = null;

        if (dto.getId() == null)
        {
            jpa = new JndiObjectResource();
        }
        else
        {
            jpa = em.find(JndiObjectResource.class, dto.getId());
        }

        jpa.setAuth(dto.getAuth());
        jpa.setDescription(dto.getDescription());
        jpa.setFactory(dto.getFactory());
        jpa.setName(dto.getName());
        jpa.setSingleton(dto.getSingleton());
        jpa.setType(dto.getType());

        jpa = em.merge(jpa);

        List<JndiObjectResourceParameter> prmFromBefore = new ArrayList<JndiObjectResourceParameter>();
        List<JndiObjectResourceParameter> prmNow = new ArrayList<JndiObjectResourceParameter>();
        for (JndiObjectResourceParameter ee : jpa.getParameters())
        {
            prmFromBefore.add(ee);
        }

        for (ParameterDto p : dto.getParameters())
        {
            if (p.getKey() == null || p.getKey().isEmpty() || p.getValue() == null || p.getValue().isEmpty())
            {
                continue;
            }

            JndiObjectResourceParameter np = null;
            if (p.getId() == null)
            {
                np = new JndiObjectResourceParameter();
            }
            else
            {
                np = em.find(JndiObjectResourceParameter.class, p.getId());
            }
            np.setKey(p.getKey());
            np.setValue(p.getValue());
            np.setResource(jpa);
            np = em.merge(np);
            prmNow.add(np);
        }

        // Remove parameters that are not present anymore
        ml: for (JndiObjectResourceParameter presentbefore : prmFromBefore)
        {
            for (JndiObjectResourceParameter stillhere : prmNow)
            {
                if (stillhere.getId() == presentbefore.getId())
                {
                    continue ml;
                }
            }
            jpa.getParameters().remove(presentbefore);
            em.remove(presentbefore);
        }

        return jpa;
    }

    private static DeploymentParameter setJpa(EntityManager em, QueueMappingDTO dto)
    {
        DeploymentParameter jpa = null;

        if (dto.getId() == null)
        {
            jpa = new DeploymentParameter();
        }
        else
        {
            jpa = em.find(DeploymentParameter.class, dto.getId());
        }

        // Update or set fields
        jpa.setNbThread(dto.getNbThread());
        jpa.setNode(em.find(Node.class, dto.getNodeId()));
        jpa.setPollingInterval(dto.getPollingInterval());
        jpa.setQueue(em.find(Queue.class, dto.getQueueId()));

        // Save
        jpa = em.merge(jpa);

        // Done
        return jpa;
    }

    private static Queue setJpa(EntityManager em, QueueDTO dto)
    {
        Queue jpa = null;

        if (dto.getId() == null)
        {
            jpa = new Queue();
        }
        else
        {
            jpa = em.find(Queue.class, dto.getId());
        }

        // Update or set fields
        jpa.setDefaultQueue(dto.isDefaultQueue());
        jpa.setDescription(dto.getDescription());
        jpa.setName(dto.getName());
        jpa.setTimeToLive(-1);

        // save
        jpa = em.merge(jpa);

        // Done
        return jpa;
    }

}