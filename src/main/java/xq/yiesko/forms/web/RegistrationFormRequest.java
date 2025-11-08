package xq.yiesko.forms.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.jboss.resteasy.reactive.RestForm;
import xq.yiesko.forms.service.dto.RegistrationFormCommand;
import xq.yiesko.forms.web.validation.AcceptTerms;
import xq.yiesko.forms.web.validation.ValidDate;
import xq.yiesko.forms.web.validation.ValidEmail;

import java.time.LocalDate;

import static xq.yiesko.forms.web.validation.ValidDateValidator.formatForInput;
import static xq.yiesko.forms.web.validation.ValidDateValidator.parseOrNull;

@SuppressWarnings("SpellCheckingInspection")
public class RegistrationFormRequest {

    @RestForm("nomeCompleto")
    @Pattern(
            regexp = "^[\\p{L}\\s'-]{3,180}$",
            message = "Informe o nome completo (mínimo 3 caracteres, apenas letras, espaços, hífens e apóstrofos)."
    )
    public String nomeCompleto;

    @RestForm("endereco")
    @NotBlank(message = "Informe o endereço.")
    @Pattern(
            regexp = "^[\\p{L}0-9\\s.,º°ª-]{5,255}$",
            message = "O endereço deve ter entre 5 e 255 caracteres."
    )
    public String endereco;

    @RestForm("rua")
    @NotBlank(message = "Informe a rua.")
    @Pattern(
            regexp = "^[\\p{L}0-9\\s.,º°ª-]{3,150}$",
            message = "A rua deve ter entre 3 e 150 caracteres."
    )
    public String rua;

    @RestForm("numero")
    @Pattern(
            regexp = "^[0-9A-Za-z\\s/-]{1,32}$",
            message = "Informe o número (1 a 32 caracteres: dígitos, letras, espaços, hífens ou barras)."
    )
    public String numero;

    @RestForm("dataNascimento")
    @NotBlank(message = "Informe a data de nascimento.")
    @ValidDate(message = "Use o formato DD-MM-AAAA (ex: 15-05-1990) e verifique se a data é válida.")
    public String dataNascimento;

    @RestForm("email")
    @ValidEmail()
    public String email;

    @RestForm("cidade")
    @Pattern(
            regexp = "^[\\p{L}\\s'.-]{2,120}$",
            message = "Informe a cidade (mínimo 2 caracteres, apenas letras, espaços, hífens, pontos e apóstrofos)."
    )
    public String cidade;

    @RestForm("estado")
    @Pattern(
            regexp = "^[A-Z]{2}$",
            message = "Informe a sigla do estado com 2 letras maiúsculas (ex: PI, SP)."
    )
    public String estado;

    @RestForm("aceite")
    @AcceptTerms()
    public Boolean aceite;

    public RegistrationFormCommand toCommand() {
        return new RegistrationFormCommand(
                trimToNull(nomeCompleto),
                trimToNull(endereco),
                trimToNull(rua),
                trimToNull(numero),
                parseDataNascimento(),
                trimToNull(email),
                trimToNull(cidade),
                trimToNull(estado),
                Boolean.TRUE.equals(aceite)
        );
    }

    public LocalDate parseDataNascimento() {
        var raw = trimToNull(dataNascimento);
        if (raw == null) return null;

        var parsed = parseOrNull(raw);

        if (parsed != null) dataNascimento = formatForInput(parsed);

        return parsed;
    }

    public RegistrationFormRequest withDefaults() {
        if (aceite == null) aceite = Boolean.TRUE;
        return this;
    }

    private String trimToNull(
            String value
    ) {
        if (value == null) return null;

        var trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}