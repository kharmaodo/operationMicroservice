package sn.free.selfcare.client;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import sn.free.selfcare.service.dto.asset.GroupeAdjustmentDTO;

@AuthorizedFeignClient(name = "assetmicroservice")
public interface AssetFeignClient {

	@RequestMapping(method = RequestMethod.GET, value = "/api/groupes/adjustment/{id}")
	GroupeAdjustmentDTO getGroupeForAdjustment(@PathVariable(name = "id") Long id);

	@RequestMapping(method = RequestMethod.GET, value = "/api/groupes/adjustment")
	GroupeAdjustmentDTO getDefaultGroupeForAdjustment();
}
