package com.produtos.apirest.resources;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.produtos.apirest.repository.EnderecoRepository;
import com.produtos.apirest.repository.ProdutoRepository;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

import com.produtos.apirest.models.Endereco;
import com.produtos.apirest.models.Produto;



@RestController
@RequestMapping(value="/api")
@Api(value="API REST produtos")
@CrossOrigin(origins="*")
public class ProdutoResource {
	@Autowired
	ProdutoRepository produtoRepository;
	
	@Autowired
	EnderecoRepository enderecoRepository;
	
	
	@GetMapping("/produtos")
	@ApiOperation(value="Retorna uma lista de produtos")
	public List<Produto>listaProdutos(){
		return produtoRepository.findAll();
	}
	
	@GetMapping("/produto/{id}")
	@ApiOperation(value="Retorna um produto ")
	public Produto listaProdutoUnico(@PathVariable(value="id")long id){
		return produtoRepository.findById(id);
	}
	
	//passando um json com os paramentro sem a necessidade de id
	@PostMapping("/produto")
	@ApiOperation(value="Salva os produtos")
	public Produto salvaProduto(@RequestBody Produto produto){
		return produtoRepository.save(produto);
	}
	
	//deletando o arquivo passando o paramentro id json
	@DeleteMapping("/produto")
	@ApiOperation(value="deleta o produtos")
	public void deletaProduto(@RequestBody Produto produto){
		produtoRepository.delete(produto);
	}
	//Atualizar a informação passando id como paramentro json
	@PutMapping("/produto")
	@ApiOperation(value="Atualiza os produtos")
	public Produto atualizarProduto(@RequestBody Produto produto){
	  return produtoRepository.save(produto);
	}
	
	@GetMapping("/buscarCep/{cep}")
	@ApiOperation(value="Retorna um cep ")
	public  Endereco listaCepUnico(@PathVariable(value="cep")String cep) throws Exception{
		
		Endereco result = new Endereco();
		int count = cep.length();
		StringBuilder cepBuilder = new StringBuilder(cep);
		String uri = "http://api.postmon.com.br/v1/cep/{cep}";
		RestTemplate restTemplate = new RestTemplate();
		
		while (result.getCep() == null) {
			if(count == 0){
				throw new Exception("CEP nao encontrado!");
			}
			Map<String, String> params = new HashMap<String, String>();
		    params.put("cep", cepBuilder.toString());
		    try{
		    	result = restTemplate.getForObject(uri, Endereco.class, params);
		    }catch (HttpClientErrorException error){
		    	if(error.getStatusCode().equals(HttpStatus.NOT_FOUND)){
		    		cepBuilder.setCharAt(--count, '0');
		    	}
		    }
		}
	    return result;
	}

}
