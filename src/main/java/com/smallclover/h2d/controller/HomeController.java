package com.smallclover.h2d.controller;

import org.jooq.DSLContext;
import org.jooq.exception.NoDataFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smallclover.h2d.model.tables.pojos.BiogMain;
import com.smallclover.h2d.pojo2dto.Person;
import com.smallclover.h2d.pojo2dto.Relationship;
import com.smallclover.h2d.pojo2dto.Status;

import static com.smallclover.h2d.model.Tables.*;
import static org.jooq.impl.DSL.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collector;
import java.util.stream.Collectors;


@Controller
@RequestMapping("")
public class HomeController {
	
	@Autowired
	private DSLContext dsl;
	
	@RequestMapping(value = {"", "/"})
	public String home(){
		return "home";
	}

	@RequestMapping(value = "/search")
	public String index(@RequestParam("search-cond") String searchCond, Model model) {
		List<BiogMain> biogMainList = dsl.selectFrom(BIOG_MAIN)
			.where(
				BIOG_MAIN.C_NAME.eq(searchCond)
				.or(BIOG_MAIN.C_NAME_CHN.eq(searchCond))
				.or(upper(BIOG_MAIN.C_NAME).like("%" + searchCond+"%"))
				.or(upper(BIOG_MAIN.C_NAME_CHN).like("%" + searchCond+"%"))
			)
			.limit(20).fetchInto(BiogMain.class);
		model.addAttribute("biogMains", biogMainList);
		return "index";
	}

	@RequestMapping(value = "/person-detail/{person_id}")
	public String detail(@PathVariable("person_id") Integer personId, Model model) {
		try{
			Person person = dsl.select(
				BIOG_MAIN.C_PERSONID, 
				BIOG_MAIN.C_NAME,
				BIOG_MAIN.C_INDEX_YEAR,
				BIOG_MAIN.C_FEMALE,
				BIOG_MAIN.C_NAME_CHN, 
				BIOG_MAIN.C_BIRTHYEAR, 
				BIOG_MAIN.C_DEATHYEAR,
				DYNASTIES.C_DYNASTY_CHN,
				ALTNAME_DATA.C_ALT_NAME_CHN
			)
			.from(BIOG_MAIN)
			.leftJoin(DYNASTIES).on(DYNASTIES.C_DY.eq(BIOG_MAIN.C_DY))
			.leftJoin(ALTNAME_DATA).on(ALTNAME_DATA.C_PERSONID.eq(BIOG_MAIN.C_PERSONID).and(ALTNAME_DATA.C_ALT_NAME_TYPE_CODE.eq(4)))// 字
			.where(BIOG_MAIN.C_PERSONID.eq(personId))
			.fetchSingle()
			.into(Person.class);

			model.addAttribute("person", person);

		}catch(NoDataFoundException except){
			except.printStackTrace();
			return "common/error";
		}

		return "detail";
	}

	@RequestMapping(value = "/person-relationship/{person_id}")
	public String relationship(@PathVariable("person_id") Integer personId, Model model) {
		List<Relationship> relationshipList = dsl.select(
				BIOG_MAIN.C_PERSONID, 
				BIOG_MAIN.C_NAME,
				BIOG_MAIN.C_NAME_CHN, 
				BIOG_MAIN.C_INDEX_YEAR,
				ASSOC_TYPES.C_ASSOC_TYPE_DESC_CHN,
				ASSOC_CODES.C_ASSOC_DESC_CHN
			)
			.from(ASSOC_DATA)
			.leftJoin(BIOG_MAIN).on(BIOG_MAIN.C_PERSONID.eq(ASSOC_DATA.C_ASSOC_ID))
			.leftJoin(ASSOC_CODES).on(ASSOC_CODES.C_ASSOC_CODE.eq(ASSOC_DATA.C_ASSOC_CODE))
			.leftJoin(ASSOC_CODE_TYPE_REL).on(ASSOC_CODE_TYPE_REL.C_ASSOC_CODE.eq(ASSOC_CODES.C_ASSOC_CODE))
			.leftJoin(ASSOC_TYPES).on(ASSOC_TYPES.C_ASSOC_TYPE_ID.eq(ASSOC_CODE_TYPE_REL.C_ASSOC_TYPE_ID))
			.leftJoin(DYNASTIES).on(DYNASTIES.C_DY.eq(BIOG_MAIN.C_DY))
			.where(ASSOC_DATA.C_PERSONID.eq(personId)).fetch().into(Relationship.class);

		BiogMain biogMain = dsl.selectFrom(BIOG_MAIN).where(BIOG_MAIN.C_PERSONID.eq(personId)).fetchOne().into(BiogMain.class);

		Map<Integer,List<Relationship>> relationshipMap = relationshipList.stream()
			.collect(Collectors.groupingBy(Relationship::getCPersonid, LinkedHashMap::new, Collectors.toList()));


		model.addAttribute("biogMain", biogMain);	
		model.addAttribute("relationshipMap", relationshipMap);
		return "relationship";
	}

	@RequestMapping(value = "/person-status/{person_id}")
	public String status(@PathVariable("person_id") Integer personId, Model model) {
		List<Status> statusList = dsl.select(
				BIOG_MAIN.C_PERSONID, 
				BIOG_MAIN.C_NAME,
				BIOG_MAIN.C_NAME_CHN, 
				STATUS_CODES.C_STATUS_DESC_CHN,
				STATUS_TYPES.C_STATUS_TYPE_CHN
			)
			.from(BIOG_MAIN)
			.leftJoin(STATUS_DATA).on(STATUS_DATA.C_PERSONID.eq(BIOG_MAIN.C_PERSONID))
			.leftJoin(STATUS_CODES).on(STATUS_CODES.C_STATUS_CODE.eq(STATUS_DATA.C_STATUS_CODE))
			.leftJoin(STATUS_CODE_TYPE_REL).on(STATUS_CODE_TYPE_REL.C_STATUS_CODE.eq(STATUS_CODES.C_STATUS_CODE))
			.leftJoin(STATUS_TYPES).on(STATUS_TYPES.C_STATUS_TYPE_CODE.eq(STATUS_CODE_TYPE_REL.C_STATUS_TYPE_CODE))
			.where(BIOG_MAIN.C_PERSONID.eq(personId)).fetch().into(Status.class);

		BiogMain biogMain = dsl.selectFrom(BIOG_MAIN).where(BIOG_MAIN.C_PERSONID.eq(personId)).fetchOne().into(BiogMain.class);
		model.addAttribute("biogMain", biogMain);	
		model.addAttribute("statusList", statusList);
		return "status";
	}
}