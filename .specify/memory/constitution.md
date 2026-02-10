<!--
Sync Impact Report:
Version change: 1.0.0 → 1.0.1
Modified principles:
  - Test Quality First: Changed test pattern from AAA (Arrange-Act-Assert) to Given/When/Then (Gherkin syntax)
Added sections: None
Removed sections: None
Templates status:
  ✅ plan-template.md - aligned
  ✅ spec-template.md - aligned (already uses Given/When/Then)
  ✅ tasks-template.md - aligned
  ✅ All command files reviewed
Follow-up: None
-->

# PITest Minimal Constitution

## Core Principles

### I. Test Quality First (NON-NEGOTIABLE)

All production code MUST be covered by meaningful tests that verify behavior, not implementation. Tests MUST:
- Follow the Given/When/Then (Gherkin) pattern for clarity and readability
- Test one logical behavior per test method
- Have descriptive names that communicate intent (e.g., `shouldReturnEmptyListWhenNoItemsExist`)
- Structure test logic: **Given** (setup/preconditions), **When** (action), **Then** (assertions/outcomes)
- Be independent and executable in any order
- Fail for the right reasons - changing implementation details should not break tests

**Rationale**: High-quality tests are the foundation for confident refactoring and mutation testing effectiveness. Poor tests lead to false confidence and wasted CI/CD resources.

### II. Mutation Coverage Standards

Mutation testing MUST achieve minimum thresholds before code can be merged:
- Line coverage: ≥ 80%
- Mutation coverage: ≥ 75%
- All mutants in critical paths (security, data integrity, business logic) MUST be killed
- Surviving mutants MUST be documented with justification (equivalent mutants, unreachable code, etc.)

**Rationale**: Line coverage alone is insufficient. Mutation testing ensures tests actually verify correctness by introducing defects and validating that tests catch them.

### III. Code Maintainability

Code MUST prioritize readability and maintainability over cleverness:
- Methods MUST not exceed 30 lines (excluding whitespace and comments)
- Cyclomatic complexity MUST not exceed 10 per method
- Classes MUST have a single, well-defined responsibility
- Magic numbers and strings MUST be replaced with named constants
- Comments MUST explain "why", not "what" - code should be self-documenting

**Rationale**: Maintainable code reduces cognitive load, accelerates onboarding, and minimizes defect introduction during changes.

### IV. Performance Requirements

All features MUST meet performance baselines:
- Mutation test execution: MUST complete within 5 minutes for full suite
- Individual test execution: MUST complete within 100ms (unit tests) or 1s (integration tests)
- Memory usage: MUST not exceed 512MB heap for test execution
- Build time: MUST not exceed 2 minutes for clean build with all checks

**Rationale**: Fast feedback loops are essential for developer productivity. Performance regressions in the test suite compound over time and erode development velocity.

## Code Quality Standards

### Static Analysis

All code MUST pass static analysis without warnings:
- Checkstyle: Enforce consistent code formatting and style conventions
- SpotBugs: Detect common bug patterns and anti-patterns
- PMD: Identify code smells and potential issues
- Compiler warnings: Treat all warnings as errors (-Werror equivalent)

### Test Organization

Tests MUST be organized following these conventions:
- Unit tests: `src/test/java/` matching production package structure
- Integration tests: `src/test/java/` with `IT` suffix (e.g., `UserServiceIT.java`)
- Test fixtures: Centralized in test utility classes, never duplicated
- Test data: Use builders or factories, not verbose constructors

### Documentation

Documentation MUST be maintained for:
- Public APIs: Javadoc with examples for all public methods and classes
- Configuration: Clear descriptions of all configurable parameters
- Mutation testing results: Summary reports committed with each release
- Known limitations: Document any surviving mutants and their justification

## Development Workflow

### Pre-Commit Requirements

Before committing, developers MUST verify:
1. All tests pass locally
2. Code compiles without warnings
3. Static analysis passes without violations
4. Mutation tests executed for changed code (minimum thresholds met)

### Code Review Requirements

All changes MUST undergo code review verifying:
- Constitutional compliance with all principles
- Test coverage meets minimum thresholds
- Mutation coverage meets minimum thresholds
- Performance impact assessed (benchmarks for performance-sensitive changes)
- Documentation updated to reflect changes

### Continuous Integration Gates

CI pipeline MUST enforce:
1. Compilation success
2. All unit tests pass
3. All integration tests pass
4. Static analysis violations = 0
5. Line coverage ≥ 80%
6. Mutation coverage ≥ 75%
7. Build time ≤ 2 minutes
8. No dependency vulnerabilities (critical/high severity)

Any gate failure MUST block merge.

## Governance

This constitution supersedes all other development practices and guidelines. All code changes, architecture decisions, and process modifications MUST align with these principles.

### Amendment Process

Amendments require:
1. Written proposal with rationale and impact analysis
2. Team consensus (≥ 80% approval)
3. Migration plan for existing code if applicable
4. Version increment following semantic versioning

### Compliance Review

Quarterly reviews MUST verify:
- Adherence to all principles across codebase
- Effectiveness of quality gates
- Team feedback on principle practicality
- Metrics trends (coverage, build time, defect rates)

### Version Control

Constitution versions follow semantic versioning:
- **MAJOR**: Backward-incompatible principle changes or removals
- **MINOR**: New principles added or substantial expansions
- **PATCH**: Clarifications, wording improvements, non-semantic refinements

**Version**: 1.0.1 | **Ratified**: 2026-02-10 | **Last Amended**: 2026-02-10
