# Recipe Application

A recipe management application deployed using Crossplane on OpenShift.

## Project Structure

```
.
├── definitions/                  # Infrastructure definitions
│   ├── crossplane/              # Crossplane configurations
│   │   ├── project/            # Project-level Crossplane definitions
│   │   │   └── crossplane-project.yaml
│   │   └── application/        # Application-level Crossplane definitions
│   │       └── crossplane-config.yaml
│   └── docker/                 # Docker configurations
│       ├── docker-compose.yml
│       └── docker-compose.prod.yml
├── frontend-ca2/               # Frontend application
├── backend-ca2/               # Backend application
└── .github/                   # GitHub Actions workflows
    └── workflows/
        └── crossplane-deploy.yml
```

## Setup

1. **Prerequisites**:
   - OpenShift cluster access
   - GitHub repository
   - GitHub CLI installed

2. **Configure GitHub Secrets**:
   - Add `OPENSHIFT_TOKEN` with your OpenShift authentication token

3. **Deployment**:
   - Push to main branch to trigger deployment
   - GitHub Actions will handle the rest

## Development

- Frontend: Node.js application in `frontend-ca2/`
- Backend: Java Spring Boot application in `backend-ca2/`

## Infrastructure

The application is deployed using Crossplane with the following components:

1. **Project Setup** (`definitions/crossplane/project/`):
   - OpenShift project creation
   - Service account setup
   - Role bindings

2. **Application Deployment** (`definitions/crossplane/application/`):
   - Frontend deployment
   - Backend deployment
   - MongoDB setup
   - Route configuration

3. **Docker Configuration** (`definitions/docker/`):
   - Development environment
   - Production environment 