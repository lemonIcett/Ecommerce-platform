\# Contributing Guide



\## Branch Strategy



| Branch | Purpose |

|---|---|

| `main` | Production-ready code only. Never commit directly. |

| `dev` | Integration branch. All features merge here first. |

| `feature/\*` | One branch per feature. Branch off `dev`. |



\## Workflow



1\. Create a feature branch from `dev`

```bash

git checkout dev

git checkout -b feature/your-feature-name

```



2\. Make your changes with meaningful commits

```bash

git add .

git commit -m "feat(service-name): short description of what you did"

```



3\. Push and open a Pull Request into `dev`

```bash

git push origin feature/your-feature-name

```



4\. `dev` merges into `main` only when a full feature is complete and tested.



\## Commit Message Format

type(scope): short description



| Type | When to use |

|---|---|

| `feat` | Adding new functionality |

| `fix` | Fixing a bug |

| `chore` | Config, setup, dependencies |

| `test` | Adding or updating tests |

| `docs` | Documentation only |

| `ci` | CI/CD pipeline changes |



\### Examples

feat(user-service): add JWT refresh token endpoint

fix(order-service): handle null product response

chore: add docker-compose base configuration

test(product-service): add unit tests for ProductService

ci: add GitHub Actions build pipeline

