# nx-starter

Template Nx monorepo with a frontend, a backend and infrastructure as code —
plus the lint, architecture and agent guardrails already wired up.

| Project   | Path       | Stack                                                     |
| --------- | ---------- | --------------------------------------------------------- |
| `web`     | `apps/web` | Angular 22, NgRx Signal Store, Sheriff + tsarch boundaries |
| `api`     | `apps/api` | Spring Boot 4 (Java 25, Gradle), clean architecture + ArchUnit |
| `ui`      | `libs/ui`  | Zard/shadcn design system on Tailwind v4                   |
| `infra`   | `infra`    | Terraform for Google Cloud (Cloud Run, Cloud SQL, VPC, WIF) |
| `scripts` | `scripts`  | Node tooling for the checks and agent hooks                |

The web app talks to the API through a small greeting feature that exists as a
vertical slice example: domain → application → infrastructure on the API side,
feature → data-access → UI on the web side. Delete it once your own first
feature lands.

## Getting started

```bash
npm install
npm run dev
```

`npm run dev` serves the Angular app; `nx run api:bootRun` starts the Spring
Boot API. `compose.yaml` brings up the local Postgres the API expects.

Requirements: Node (see `.nvmrc`), JDK 25, Docker for the local database, and
Terraform + `gcloud` if you deploy the infrastructure.

## Everyday commands

```bash
npm run build          # nx run-many --target=build
npm run test           # nx run-many --target=test
npm run lint           # nx run-many --target=lint
npm run verify         # full checks for every project
npm run verify -- --changed   # only projects with uncommitted changes
```

## Using this template

1. Create a repository from this template on GitHub.
2. Rename the workspace: `nx-starter` in `package.json`, `@nx-starter/web`,
   the Java package `com.example.nxstarter` (and `group` in
   `apps/api/build.gradle`), `app_name` in `infra/environments/dev/locals.tf`,
   and the brand strings in `apps/web/src`.
3. Swap the `--brand-*` tokens at the top of `libs/ui/styles.css` for your own
   palette — every component reads the semantic tokens derived from them.
4. To deploy, follow `infra/README.md`: run the bootstrap stack once, then set
   the GitHub Actions repository variables (`GCP_PROJECT_ID`, `GCP_REGION`,
   `GCP_WORKLOAD_IDENTITY_PROVIDER`, `GCP_DEPLOYER_SERVICE_ACCOUNT`,
   optionally `APP_DOMAIN`). No secrets are stored in the repository.

## Guardrails

Architecture rules are enforced, not just documented: Sheriff and tsarch on the
web side, ArchUnit and Spotless on the API side. They run on the husky
pre-commit hook and in CI, scoped to the projects whose files changed. Fix the
code rather than weakening a rule.

Agent configuration lives in `.agents/` directories (root and per app) and is
generated into `.claude/` / `.cursor/` copies by
`npm run sync:agent-config`. See `AGENTS.md` for the full contract.
