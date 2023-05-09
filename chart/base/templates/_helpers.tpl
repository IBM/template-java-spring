{{/*
Expand the name of the chart.
*/}}
{{- define "starter-kit.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Create a default fully qualified app name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
If release name contains chart name it will be used as a full name.
*/}}
{{- define "starter-kit.fullname" -}}
{{- if .Values.fullnameOverride }}
{{- .Values.fullnameOverride | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- $name := default .Chart.Name .Values.nameOverride }}
{{- if contains $name .Release.Name }}
{{- .Release.Name | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" }}
{{- end }}
{{- end }}
{{- end }}

{{/*
Create chart name and version as used by the chart label.
*/}}
{{- define "starter-kit.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Common labels
*/}}
{{- define "starter-kit.labels" -}}
helm.sh/chart: {{ include "starter-kit.chart" . }}
{{ include "starter-kit.selectorLabels" . }}
{{- if .Chart.AppVersion }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end }}
{{- if .Values.partOf }}
app.kubernetes.io/part-of: {{ .Values.partOf }}
{{- end}}
{{- if .Values.runtime }}
app.openshift.io/runtime: {{ .Values.runtime }}
{{- end}}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}

{{- define "starter-kit.annotations" -}}
kubernetes.io/description: {{ .Values.openapi.description | quote }}
{{- if and .Values.vcsInfo.repoUrl .Values.vcsInfo.branch }}
app.openshift.io/vcs-ref: {{ .Values.vcsInfo.branch }}
app.openshift.io/vcs-uri: {{ .Values.vcsInfo.repoUrl }}
{{- end }}
{{- if .Values.connectsTo }}
app.openshift.io/connects-to: {{ .Values.connectsTo }}
{{- end }}
{{- end }}


{{/*
Selector labels
*/}}
{{- define "starter-kit.selectorLabels" -}}
app.kubernetes.io/name: {{ include "starter-kit.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end }}

{{/*
Create the name of the service account to use
*/}}
{{- define "starter-kit.serviceAccountName" -}}
{{- if .Values.serviceAccount.create }}
{{- default (include "starter-kit.fullname" .) .Values.serviceAccount.name }}
{{- else }}
{{- default "default" .Values.serviceAccount.name }}
{{- end }}
{{- end }}
