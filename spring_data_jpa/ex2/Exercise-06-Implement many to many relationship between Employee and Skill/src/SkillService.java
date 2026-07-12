/** Service layer for Skill - @Service, wraps SkillRepository. */
@Service
public class SkillService {

    private static final Logger LOGGER = Logger.getLogger(SkillService.class);

    private final SkillRepository skillRepository =
            RepositoryFactory.create(SkillRepository.class, Skill.class);

    @Transactional
    public Skill get(int id) {
        LOGGER.info("Start");
        Skill skill = skillRepository.findById(id).get();
        LOGGER.info("End");
        return skill;
    }

    @Transactional
    public void save(Skill skill) {
        LOGGER.info("Start");
        skillRepository.save(skill);
        LOGGER.info("End");
    }
}
