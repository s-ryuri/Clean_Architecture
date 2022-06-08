package gdsc.architecture;

import gdsc.architecture.service.MemberService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class ArchitectureApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArchitectureApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}


	@Bean
	CommandLineRunner run(MemberService memberService){
		return args -> {
			memberService.signUp("fbfbf1@naver.com","ryool1","abcdefghijkg");
			memberService.signUp("fbfbf2@naver.com","ryool2","abcdefghijkg");
			memberService.signUp("fbfbf3@naver.com","ryool3","abcdefghijkg");
		};
	}


}
