const regex = {
    nombrePaciente: /^[A-Za-z0-9 ]{1,40}$/,
    nombreMedico: /^[A-Za-z ]{1,60}$/,
    apellido: /^[A-Za-z ]{1,60}$/,
    especialidad: /^(Medicina general|Pediatria|Dermatologia|Cardiologia|Odontologia|Ginecologia|Ortopedia)$/,
    texto60: /^[\s\S]{1,60}$/,
    username: /^[A-Za-z0-9]{1,30}$/,
    documento: /^[0-9]{1,20}$/,
    motivoConsulta: /^[\s\S]{1,100}$/,
    consultorio: /^([1-9]|1[0-9]|20)$/,
    hora: /^([01]\d|2[0-3]):[0-5]\d$/
};

function limpiarErrores(formulario) {
    formulario.querySelectorAll(".is-invalid").forEach(campo => campo.classList.remove("is-invalid"));
    formulario.querySelectorAll(".invalid-feedback").forEach(error => error.textContent = "");
}

function marcarError(campo, mensaje) {
    campo.classList.add("is-invalid");
    const contenedor = campo.closest(".mb-3") || campo.parentElement;
    const error = contenedor.querySelector(".invalid-feedback");

    if (error) {
        error.textContent = mensaje;
    }
}

function validarHorario(horaInicio, horaFin, campoFin) {
    if (!regex.hora.test(horaInicio)) {
        return "La hora de inicio debe tener formato HH:mm.";
    }

    if (!regex.hora.test(horaFin)) {
        return "La hora de finalizacion debe tener formato HH:mm.";
    }

    if (horaFin <= horaInicio) {
        marcarError(campoFin, "La hora de finalizacion debe ser mayor a la hora de inicio.");
        return "La hora de finalizacion debe ser mayor a la hora de inicio.";
    }

    return "";
}

function cuerpoDesdeFormularioConsulta(formulario) {
    return {
        nombrePaciente: formulario.nombrePaciente.value.trim(),
        motivoConsulta: formulario.motivoConsulta.value.trim(),
        numeroConsultorio: Number(formulario.numeroConsultorio.value),
        horaInicio: formulario.horaInicio.value,
        horaFin: formulario.horaFin.value,
        medicoId: Number(formulario.medicoId.value),
        pacienteId: Number(formulario.pacienteId.value)
    };
}

function validarConsulta(formulario) {
    limpiarErrores(formulario);

    const datos = cuerpoDesdeFormularioConsulta(formulario);
    let valido = true;

    if (!regex.nombrePaciente.test(datos.nombrePaciente)) {
        marcarError(formulario.nombrePaciente, "Use solo letras, numeros y espacios. Maximo 40 caracteres.");
        valido = false;
    }

    if (!regex.motivoConsulta.test(datos.motivoConsulta)) {
        marcarError(formulario.motivoConsulta, "El motivo es obligatorio y maximo de 100 caracteres.");
        valido = false;
    }

    if (!regex.consultorio.test(String(datos.numeroConsultorio))) {
        marcarError(formulario.numeroConsultorio, "Ingrese un consultorio numerico entre 1 y 20.");
        valido = false;
    }

    if (!regex.hora.test(datos.horaInicio)) {
        marcarError(formulario.horaInicio, "La hora de inicio debe tener formato HH:mm.");
        valido = false;
    }

    if (!regex.hora.test(datos.horaFin)) {
        marcarError(formulario.horaFin, "La hora de finalizacion debe tener formato HH:mm.");
        valido = false;
    }

    if (regex.hora.test(datos.horaInicio) && regex.hora.test(datos.horaFin) && datos.horaFin <= datos.horaInicio) {
        marcarError(formulario.horaFin, "La hora de finalizacion debe ser mayor a la hora de inicio.");
        valido = false;
    }

    if (!datos.medicoId) {
        marcarError(formulario.medicoId, "Seleccione un medico.");
        valido = false;
    }

    if (!datos.pacienteId) {
        marcarError(formulario.documentoPaciente, "Busque y seleccione un paciente registrado.");
        valido = false;
    }

    return valido ? datos : null;
}

async function enviarJson(url, metodo, datos) {
    const respuesta = await fetch(url, {
        method: metodo,
        headers: {
            "Content-Type": "application/json",
            "Accept": "application/json"
        },
        body: datos ? JSON.stringify(datos) : undefined
    });

    if (!respuesta.ok) {
        let mensaje = "No fue posible completar la solicitud.";

        try {
            const error = await respuesta.json();
            mensaje = error.mensaje || mensaje;
        } catch (e) {
            mensaje = respuesta.statusText || mensaje;
        }

        throw new Error(mensaje);
    }

    if (respuesta.status === 204) {
        return null;
    }

    return respuesta.json();
}

function configurarFormularioConsulta() {
    const formulario = document.getElementById("formConsulta");
    const modal = document.getElementById("modalConsulta");

    if (!formulario || !modal) {
        return;
    }

    modal.addEventListener("show.bs.modal", event => {
        limpiarErrores(formulario);
        formulario.reset();
        formulario.elements["id"].value = "";
        formulario.pacienteId.value = "";
        const resultado = document.getElementById("resultadoPaciente");
        if (resultado) {
            resultado.textContent = "";
            resultado.className = "form-text";
        }

        const boton = event.relatedTarget;

        if (!boton || !boton.classList.contains("btn-editar-consulta")) {
            modal.querySelector(".modal-title").textContent = "Crear Consulta";
            return;
        }

        modal.querySelector(".modal-title").textContent = "Editar Consulta";
        formulario.elements["id"].value = boton.dataset.id;
        formulario.nombrePaciente.value = boton.dataset.nombre;
        formulario.motivoConsulta.value = boton.dataset.motivo;
        formulario.numeroConsultorio.value = boton.dataset.consultorio;
        formulario.horaInicio.value = boton.dataset.inicio;
        formulario.horaFin.value = boton.dataset.fin;
        formulario.medicoId.value = boton.dataset.medico;
        formulario.pacienteId.value = boton.dataset.paciente;
        formulario.documentoPaciente.value = "Paciente ya asignado";
        if (resultado) {
            resultado.textContent = "Paciente seleccionado para esta consulta.";
            resultado.className = "form-text text-success";
        }
    });

    formulario.addEventListener("submit", async event => {
        event.preventDefault();

        const datos = validarConsulta(formulario);
        if (!datos) {
            return;
        }

        const id = formulario.elements["id"].value;
        const url = id ? `/api/consultas/${id}` : "/api/consultas";
        const metodo = id ? "PUT" : "POST";

        try {
            await enviarJson(url, metodo, datos);
            window.location.reload();
        } catch (error) {
            alert(error.message);
        }
    });
}

function configurarBusquedaPaciente() {
    const boton = document.getElementById("btnBuscarPaciente");
    const formulario = document.getElementById("formConsulta");
    const resultado = document.getElementById("resultadoPaciente");

    if (!boton || !formulario || !resultado) {
        return;
    }

    boton.addEventListener("click", async () => {
        limpiarErrores(formulario);
        formulario.pacienteId.value = "";
        resultado.textContent = "";
        resultado.className = "form-text";

        const documento = formulario.documentoPaciente.value.trim();
        if (!regex.documento.test(documento)) {
            marcarError(formulario.documentoPaciente, "Ingrese un documento numerico valido.");
            return;
        }

        try {
            const paciente = await enviarJson(`/api/pacientes/buscar?documento=${encodeURIComponent(documento)}`, "GET");
            formulario.pacienteId.value = paciente.id;
            formulario.nombrePaciente.value = paciente.nombreCompleto;
            resultado.textContent = `Se encontro: ${paciente.nombreCompleto} - documento ${paciente.documento}`;
            resultado.className = "form-text text-success";
        } catch (error) {
            resultado.textContent = "No se encontro un paciente con ese documento.";
            resultado.className = "form-text text-danger";
        }
    });
}

function cuerpoDesdeFormularioMedico(formulario) {
    return {
        nombre: formulario.nombre.value.trim(),
        apellido: formulario.apellido.value.trim(),
        especialidad: formulario.especialidad.value.trim(),
        username: formulario.username.value.trim(),
        password: formulario.password.value
    };
}

function validarMedico(formulario) {
    limpiarErrores(formulario);

    const datos = cuerpoDesdeFormularioMedico(formulario);
    const esEdicion = Boolean(formulario.elements["id"].value);
    let valido = true;

    if (!regex.nombreMedico.test(datos.nombre)) {
        marcarError(formulario.nombre, "Use solo letras y espacios. Maximo 60 caracteres.");
        valido = false;
    }

    if (!regex.apellido.test(datos.apellido)) {
        marcarError(formulario.apellido, "Use solo letras y espacios. Maximo 60 caracteres.");
        valido = false;
    }

    if (!regex.especialidad.test(datos.especialidad)) {
        marcarError(formulario.especialidad, "Seleccione una especialidad valida.");
        valido = false;
    }

    if (!regex.username.test(datos.username)) {
        marcarError(formulario.username, "Use solo letras y numeros. Maximo 30 caracteres.");
        valido = false;
    }

    if (!esEdicion && !datos.password.trim()) {
        marcarError(formulario.password, "La contrasena es obligatoria al crear un medico.");
        valido = false;
    }

    return valido ? datos : null;
}

function configurarFormularioMedico() {
    const formulario = document.getElementById("formMedico");
    const modal = document.getElementById("modalMedico");

    if (!formulario || !modal) {
        return;
    }

    modal.addEventListener("show.bs.modal", event => {
        limpiarErrores(formulario);
        formulario.reset();
        formulario.elements["id"].value = "";
        formulario.username.disabled = false;

        const boton = event.relatedTarget;
        if (!boton || !boton.classList.contains("btn-editar-medico")) {
            modal.querySelector(".modal-title").textContent = "Crear Medico";
            return;
        }

        modal.querySelector(".modal-title").textContent = "Editar Medico";
        formulario.elements["id"].value = boton.dataset.id;
        formulario.nombre.value = boton.dataset.nombre;
        formulario.apellido.value = boton.dataset.apellido;
        formulario.especialidad.value = boton.dataset.especialidad;
        formulario.username.value = boton.dataset.username;
        formulario.username.disabled = true;
    });

    formulario.addEventListener("submit", async event => {
        event.preventDefault();

        const datos = validarMedico(formulario);
        if (!datos) {
            return;
        }

        const id = formulario.elements["id"].value;
        const url = id ? `/api/medicos/${id}` : "/api/medicos";
        const metodo = id ? "PUT" : "POST";

        try {
            await enviarJson(url, metodo, datos);
            window.location.reload();
        } catch (error) {
            alert(error.message);
        }
    });
}

function configurarEliminacionMedico() {
    document.querySelectorAll(".btn-eliminar-medico").forEach(boton => {
        boton.addEventListener("click", async () => {
            const confirmado = confirm("Desea eliminar este medico?");

            if (!confirmado) {
                return;
            }

            try {
                await enviarJson(`/api/medicos/${boton.dataset.id}`, "DELETE");
                window.location.reload();
            } catch (error) {
                alert(error.message);
            }
        });
    });
}

function configurarEliminacionConsulta() {
    document.querySelectorAll(".btn-eliminar-consulta").forEach(boton => {
        boton.addEventListener("click", async () => {
            const confirmado = confirm("Desea eliminar esta consulta?");

            if (!confirmado) {
                return;
            }

            try {
                await enviarJson(`/api/consultas/${boton.dataset.id}`, "DELETE");
                window.location.reload();
            } catch (error) {
                alert(error.message);
            }
        });
    });
}

function configurarFormularioHorario() {
    const formulario = document.getElementById("formHorario");
    const modal = document.getElementById("modalHorario");

    if (!formulario || !modal) {
        return;
    }

    modal.addEventListener("show.bs.modal", event => {
        limpiarErrores(formulario);
        const boton = event.relatedTarget;
        formulario.elements["id"].value = boton.dataset.id;
        formulario.horaInicio.value = boton.dataset.inicio;
        formulario.horaFin.value = boton.dataset.fin;
    });

    formulario.addEventListener("submit", async event => {
        event.preventDefault();
        limpiarErrores(formulario);

        const id = formulario.elements["id"].value;
        const datos = {
            horaInicio: formulario.horaInicio.value,
            horaFin: formulario.horaFin.value
        };

        const errorHorario = validarHorario(datos.horaInicio, datos.horaFin, formulario.horaFin);
        if (errorHorario) {
            if (!formulario.horaInicio.classList.contains("is-invalid") && !formulario.horaFin.classList.contains("is-invalid")) {
                marcarError(formulario.horaInicio, errorHorario);
            }
            return;
        }

        try {
            await enviarJson(`/api/consultas/${id}/horario`, "PUT", datos);
            window.location.reload();
        } catch (error) {
            alert(error.message);
        }
    });
}

document.addEventListener("DOMContentLoaded", () => {
    configurarFormularioConsulta();
    configurarBusquedaPaciente();
    configurarEliminacionConsulta();
    configurarFormularioMedico();
    configurarEliminacionMedico();
    configurarFormularioHorario();
});
